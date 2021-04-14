package com.teamgether.willing.model

data class Feed (
    var challengeId: String? = "",
    var pictureUrl: String? = ""
){
    override fun toString(): String {
        return "Feed(challengeId=$challengeId, pictureUrl=$pictureUrl)"
    }
}