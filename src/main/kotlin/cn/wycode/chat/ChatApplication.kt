package cn.wycode.chat

import cn.wycode.chat.service.ChatService
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.scheduling.TaskScheduler
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
class ChatApplication(val chatService: ChatService,
                      val messageTemplate: SimpMessagingTemplate) : CommandLineRunner {

    override fun run(vararg args: String?) {
        chatService.messageTemplate = messageTemplate
        chatService.generateCode()
    }
}

fun main(args: Array<String>) {
    runApplication<ChatApplication>(*args)
}
