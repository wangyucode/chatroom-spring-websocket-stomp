package cn.wycode.chat.entity

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.format.annotation.DateTimeFormat
import java.security.Principal
import java.util.*


data class CommonMessage<T>(val data: T?, val error: String?) {
    companion object {
        fun <T> success(data: T): CommonMessage<T> {
            return CommonMessage(data, null)
        }

        fun fail(error: String): CommonMessage<Nothing> {
            return CommonMessage(null, error)
        }
    }
}

open class ChatUser(val id: Int) : Principal {

    override fun getName(): String {
        return id.toString()
    }

    override fun equals(other: Any?): Boolean {
        return other is ChatUser && other.id == id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}


data class ChatMessage(val user: Int,
                       @JsonFormat(pattern = "MM-dd HH:mm:ss", timezone = "GMT+8")
                       val time: Date,
                       val content: String,
                       val type: Int)


data class InitData(val user: Int, val size: Int, val messages: List<ChatMessage>, val code: String, val gen: Int, val remove: Int)