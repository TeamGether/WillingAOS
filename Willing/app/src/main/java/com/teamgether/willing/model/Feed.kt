package com.teamgether.willing.model

data class Feed (
    var challengeId: String? = "",
    var pictureUrl: String? = "",
    var timestamp: String? = "",
    var userName: String? = ""
){
    override fun toString(): String {
        return "Feed(challengeId=$challengeId, pictureUrl=$pictureUrl)"
    }
}