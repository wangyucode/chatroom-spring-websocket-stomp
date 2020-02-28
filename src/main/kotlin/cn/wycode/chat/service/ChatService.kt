package cn.wycode.chat.service

import cn.wycode.chat.entity.ChatMessage
import cn.wycode.chat.entity.ChatUser
import cn.wycode.chat.entity.CommonMessage
import cn.wycode.chat.utils.randomString
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoField
import java.time.temporal.TemporalField
import java.util.*
import kotlin.collections.ArrayList

const val REMOVE_MESSAGE_TIME_IN_MINUTES = 3
const val ADMIN_PASSCODE = "admin"
const val GEN_CODE_TIME_IN_MINUTES = 2

@Service
class ChatService {

    lateinit var messageTemplate: SimpMessagingTemplate

    val usersPool = mutableListOf(
            ChatUser(0),
            ChatUser(1),
            ChatUser(2),
            ChatUser(3),
            ChatUser(4),
            ChatUser(5),
            ChatUser(6),
            ChatUser(7),
            ChatUser(8),
            ChatUser(9)
    )

    var code = ""
    private final val logger: Log = LogFactory.getLog(this.javaClass)
    val messages = ArrayList<ChatMessage>()
    val users = ArrayList<ChatUser>()

    fun generateCode() {
        this.code = randomString(16)
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