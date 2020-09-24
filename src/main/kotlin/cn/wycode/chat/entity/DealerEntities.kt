package cn.wycode.chat.entity

data class Room(val id: String, val users: HashSet<ChatUser>)