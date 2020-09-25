package cn.wycode.chat.entity

data class Room(val id: String, val users: HashSet<DealerUser>)

class DealerUser(id: Int, val roomId: String, var role: String? = null) : ChatUser(id)

data class DealerInitData(val userId: Int, val roomId: String, val players: HashSet<DealerUser>)

data class DealerCard(val name: String, val count: Int)