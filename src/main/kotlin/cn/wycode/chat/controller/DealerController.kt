package cn.wycode.chat.controller

import cn.wycode.chat.entity.*
import cn.wycode.chat.service.DealerService
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.springframework.messaging.Message
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.messaging.simp.annotation.SendToUser
import org.springframework.stereotype.Controller

@Controller
@MessageMapping("dealer")
class DealerController(private val dealerService: DealerService) {

    private final val logger: Log = LogFactory.getLog(this.javaClass)

    @MessageMapping("status")
    @SendToUser("/queue/dealer/status")
    fun status(accessor: SimpMessageHeaderAccessor): CommonMessage<*> {
        return if (accessor.user != null) {
            val user = accessor.user as DealerUser
            val room = dealerService.rooms[user.roomId]!!
            CommonMessage.success(DealerInitData(user.id, room.id, room.users))
        } else {
            CommonMessage.fail(accessor.sessionAttributes!!["error"] as String)
        }
    }

    @MessageMapping("start")
    fun start(message: Message<List<DealerCard>>, user: DealerUser): CommonMessage<Room> {
        return CommonMessage.success(dealerService.assignCard(user.roomId, message.payload))
    }


}
