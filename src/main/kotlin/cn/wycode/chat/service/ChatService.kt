package cn.wycode.chat.service

import cn.wycode.chat.entity.ChatMessage
import cn.wycode.chat.entity.ChatUser
import cn.wycode.chat.entity.CommonMessage
import cn.wycode.chat.utils.randomString
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

const val REMOVE_MESSAGE_TIME_IN_MINUTES = 30
const val ADMIN_PASSCODE = "admin"
const val GEN_CODE_TIME_IN_MINUTES = 10
const val MAX_USER_NUM = 10

@Service
class ChatService {

    private final val logger: Log = LogFactory.getLog(this.javaClass)

    lateinit var messageTemplate: SimpMessagingTemplate

    var userNum = 0
    var users =  HashSet<ChatUser>()
    var code = randomString(36,16)
    val messages = ArrayList<ChatMessage>()

    fun generateCode() {
        this.code = randomString(36,16)
        logger.info("${Date().toLocaleString()}: $code")
        this.sendSystemMessage(100, this.code)
        removeOutdatedMessage()
    }

    fun sendSystemMessage(type: Int, content: String) {
        val message = ChatMessage(-100, Date(), content, type)
        messageTemplate.convertAndSend("/topic/system", CommonMessage.success(message))
    }

    fun removeOutdatedMessage() {
        logger.info("${Date().toLocaleString()}: removeOutdatedMessage")
        if (messages.size > 0) {
            val message = messages[0]
            if (Date().time - message.time.time > REMOVE_MESSAGE_TIME_IN_MINUTES * 60L * 1000) {
                messages.removeAt(0)
                removeOutdatedMessage()
            }
        }
    }
}