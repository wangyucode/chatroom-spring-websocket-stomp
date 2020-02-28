package cn.wycode.chat.config

import cn.wycode.chat.service.ChatService
import cn.wycode.chat.service.GEN_CODE_TIME_IN_MINUTES
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled


@EnableScheduling
@Configuration
class Tasks(val chatService: ChatService) {

    @Scheduled(cron = "0 0/$GEN_CODE_TIME_IN_MINUTES * * * ? ") //每10分钟
    fun genCode() {
        chatService.generateCode()
    }
}