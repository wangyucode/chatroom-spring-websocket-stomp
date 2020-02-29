package cn.wycode.chat

import cn.wycode.chat.service.ChatService
import cn.wycode.chat.service.GEN_CODE_TIME_IN_MINUTES
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.scheduling.TaskScheduler

@SpringBootApplication
class ChatApplication(val messageTemplate: SimpMessagingTemplate,
                      val chatService: ChatService,
                      val taskScheduler: TaskScheduler) : CommandLineRunner {

    override fun run(vararg args: String?) {
        chatService.messageTemplate = messageTemplate
        taskScheduler.scheduleAtFixedRate({ chatService.generateCode() }, 1000L * 60 * GEN_CODE_TIME_IN_MINUTES)
    }
}

fun main(args: Array<String>) {
    runApplication<ChatApplication>(*args)
}
