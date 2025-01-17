package com.example.topwisepos.utils

object CardUtil {
    fun getCardTypFromAid(aid: String): String? {
        return if (cardType.containsKey(aid.substring(0, 10))) {
            cardType[aid.substring(0, 10)]
        } else ""
    }

    private val cardType: MutableMap<String, String> = HashMap()

    init {
        cardType["A000000004"] = "MASTER"
        cardType["A000000003"] = "VISA"
        cardType["A000000025"] = "AMEX"
        cardType["A000000065"] = "JCB"
        cardType["A000000152"] = "DISCOVER"
        cardType["A000000324"] = "DISCOVER"
        cardType["A000000333"] = "PBOC"
        cardType["A000000524"] = "RUPAY"
        cardType["A000000371"] = "VERVE"
    }
}
