package cn.wycode.chat.service

import cn.wycode.chat.entity.Room
import cn.wycode.chat.utils.random
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.springframework.stereotype.Service

const val DEALER_PASSCODE = "dealer"
const val MAX_ROOM_COUNT = 100
const val MAX_ROOM_ID = 9999
const val MAX_ROOM_PLAYER = 32

@Service
class DealerService {

    private final val logger: Log = LogFactory.getLog(this.javaClass)

    private final val roomIdPool = ArrayList<String>(MAX_ROOM_ID)
    val rooms = HashMap<String, Room>(MAX_ROOM_COUNT)

    var userNum = 0

    init {
        for (i in 0..MAX_ROOM_ID) {
            roomIdPool.add(String.format("%04d", i))
        }
    }

    fun getNewRoom(): Room {
        val index = random.nextInt(roomIdPool.size)
        val roomId = roomIdPool[index]
        roomIdPool.removeAt(index)
        return Room(roomId, HashSet(MAX_ROOM_PLAYER))
    }
}

