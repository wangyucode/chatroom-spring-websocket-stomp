package cn.wycode.chat.utils

import kotlin.random.Random


fun randomString(length: Int): String {
    val sb = StringBuilder(length)
    val random = Random(System.currentTimeMillis())
    repeat(length) {
        sb.append(random.nextInt(36).toString(36))
    }
    return sb.toString()
}

fun main() {
    println(randomString(16))
}