package com.teamgether.willing.model

data class ChallengeInfo (
    val content: String? = "",
    val count: Int? = 0,
    val donation: String? = "",
    val group:String = "",
    val money: Int? = 0,
    val percentage: Int? = 0,
    val reason:String ?= "",
    val show:Boolean ?= false,
    val success:Boolean ?= false,
    val time:String ?= "",
    val tobe:String ?= "",
    val user:String ?= "",
    val imageUrl:String ?= "",
    val userVerification:Boolean ?= false
)