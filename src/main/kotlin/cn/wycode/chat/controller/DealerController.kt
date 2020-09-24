package cn.wycode.chat.controller

import cn.wycode.chat.entity.CommonMessage
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.messaging.simp.annotation.SendToUser
import org.springframework.stereotype.Controller

@Controller
@MessageMapping("dealer")
class DealerController {

    private final val logger: Log = LogFactory.getLog(this.javaClass)

    @MessageMapping("status")
    @SendToUser("/queue/dealer/status")
    fun status(accessor: SimpMessageHeaderAccessor): CommonMessage<*> {
        return if (accessor.user != null) {
            CommonMessage.success(1)
        } else {
            CommonMessage.fail(accessor.sessionAttributes!!["error"] as String)
        }
    }
}