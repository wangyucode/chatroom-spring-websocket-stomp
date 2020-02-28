package cn.wycode.chat.config

import cn.wycode.chat.service.ChatService
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.simp.config.ChannelRegistration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.ChannelInterceptor
import org.springframework.messaging.support.MessageHeaderAccessor
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer


@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfig(val chatService: ChatService) : WebSocketMessageBrokerConfigurer {


    override fun configureMessageBroker(config: MessageBrokerRegistry) {
        config.enableSimpleBroker("/topic/", "/queue/")
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
                    if (code != null && code.size > 0 && code[0] == chatService.code) {
                        if (chatService.usersPool.size > 0) {
                            val user = chatService.usersPool.removeAt(0)
                            chatService.users.add(user)
                            accessor.user = user
                        } else {
                            accessor.sessionAttributes!!["error"] = "人数已满！"
                        }
                    } else {
                        accessor.sessionAttributes!!["error"] = "邀请码错误！"
                    }

                } else if (StompCommand.DISCONNECT == accessor.command && accessor.user != null) {
                    val id = accessor.user!!.name.toInt()
                    var index = -1
                    for ((i, user) in chatService.users.withIndex()) {
                        if (user.id == id) {
                            index = i
                            break
                        }
                    }
                    if (index >= 0) {
                        chatService.sendSystemMessage(201, id.toString())
                        chatService.usersPool.add(chatService.users.removeAt(index))
                    }
                }
                return message
            }
        })
    }
}