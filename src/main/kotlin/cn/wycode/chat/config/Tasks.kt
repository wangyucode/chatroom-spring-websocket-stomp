package cn.wycode.chat.config

import cn.wycode.chat.service.ChatService
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled

const val GEN_CODE_TIME_IN_MINUTES = 2

@EnableScheduling
@Configuration
class Tasks(val chatService: ChatService) {

    @Scheduled(cron = "0 0/$GEN_CODE_TIME_IN_MINUTES * * * ? ") //每10分钟
    fun genCode() {
        chatService.generateCode()
    }
}