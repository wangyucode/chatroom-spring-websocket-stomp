package cn.wycode.chat.utils

import cn.wycode.chat.service.DealerService
import kotlin.random.Random
val random = Random(System.currentTimeMillis())

fun randomString(radix: Int, length: Int): String {
    val sb = StringBuilder(length)
    repeat(length) {
        sb.append(random.nextInt(radix).toString(radix))
    }
    return sb.toString()
}

fun main() {
    val dealerService = DealerService()
    repeat(100){
        println(dealerService.getNewRoom())

    }

}