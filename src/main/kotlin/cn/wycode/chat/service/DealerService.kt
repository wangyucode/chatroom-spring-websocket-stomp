package cn.wycode.chat.service

import cn.wycode.chat.entity.DealerCard
import cn.wycode.chat.entity.DealerUser
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

    fun newRoom(): Room {
        val index = random.nextInt(roomIdPool.size)
        val roomId = roomIdPool[index]
        roomIdPool.removeAt(index)
        val room =  Room(roomId, HashSet(MAX_ROOM_PLAYER))
        rooms[roomId] = room
        return room
    }

    fun join(room: Room, user: DealerUser) {
        room.users.add(user)
        userNum++
    }

    fun assignCard(roomId: String, cards: List<DealerCard>): Room {
        val roles = ArrayList<String>()
        for(card in cards){
            repeat(card.count){
                roles.add(card.name)
            }
        }

        val room = rooms[roomId]!!

        for(user in room.users){
            val index = random.nextInt(roles.size)
            user.role = roles[index]
            roles.removeAt(index)
        }
        return room
    }


}

