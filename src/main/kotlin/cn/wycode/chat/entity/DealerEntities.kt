package cn.wycode.chat.entity

data class Room(val id: String, val users: HashSet<DealerUser>)

class DealerUser(id: Int, val roomId: String) : ChatUser(id)