package cn.wycode.chat.config

import cn.wycode.chat.entity.ChatUser
import cn.wycode.chat.service.ADMIN_PASSCODE
import cn.wycode.chat.service.ChatService
import cn.wycode.chat.service.MAX_USER_NUM
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.simp.config.ChannelRegistration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.ChannelInterceptor
import org.springframework.messaging.support.MessageHeaderAccessor
import org.springframework.scheduling.TaskScheduler
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer


@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfig(val chatService: ChatService, val taskScheduler: TaskScheduler) : WebSocketMessageBrokerConfigurer {


    override fun configureMessageBroker(config: MessageBrokerRegistry) {
        config.enableSimpleBroker("/topic/", "/queue/")
                .setTaskScheduler(taskScheduler)
        config.setApplicationDestinationPrefixes("/app")
    }


    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint("/stomp")
                .setAllowedOrigins("*")
    }

    override fun configureClientInboundChannel(registration: ChannelRegistration) {
        registration.interceptors(object : ChannelInterceptor {
            override fun preSend(message: Message<*>, channel: MessageChannel): Message<*>? {
                val accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor::class.java)!!
                if (StompCommand.CONNECT == accessor.command) {
                    val code = accessor.getNativeHeader("code")
                    if (code != null && code.size > 0) {
                        if (code[0] == ADMIN_PASSCODE) { //超级用户
                            accessor.sessionAttributes!!["error"] = chatService.code
                        } else if (code[0] == chatService.code) { //合法用户
                            val id = accessor.getNativeHeader("id")
                            if (id != null && id.size > 0 && id[0].toInt() > 0) { //断线重连
                                val user = ChatUser(id[0].toInt())
                                accessor.user = user
                                chatService.users.add(user)
                            } else if (chatService.users.size < MAX_USER_NUM) { //新用户
                                val user = ChatUser(chatService.userNum)
                                accessor.user = user
                                chatService.users.add(user)
                                chatService.userNum += 1
                            } else {
                                accessor.sessionAttributes!!["error"] = "人数已满！"
                            }
                        } else {
                            accessor.sessionAttributes!!["error"] = "邀请码错误！"
                        }

                    } else {
                        accessor.sessionAttributes!!["error"] = "邀请码错误！"
                    }

                } else if (StompCommand.DISCONNECT == accessor.command && accessor.user != null) {
                    val id = accessor.user!!.name.toInt()

                    if (id >= 0) {
                        if (chatService.users.remove(ChatUser(id)))
                            chatService.sendSystemMessage(201, id.toString())
                    }
                }
                return message
            }
        })
    }
}