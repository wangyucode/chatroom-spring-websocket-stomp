package cn.wycode.chat.config

import cn.wycode.chat.service.ChatService
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled


@EnableScheduling
@Configuration
class Tasks(val chatService: ChatService) {

    @Scheduled(cron = "0 * * * * ? ") //每分钟
    fun minutely() {
        chatService.removeOutdatedMessage()
    }

    @Scheduled(cron = "0 0/2 * * * ? ") //每5分钟
    fun crawlDaily() {
        chatService.generateCode()
    }
}