package com.teamgether.willing.model

data class ChallengeInfo(
    var content: String = "",
    var title: String ?= "",
    var count: String = "",
    var goal_number: String = "",
    val donation: String? = "",
    var group:String = "",
    var money: Int? = 0,
    val set: Int? = 0,
    val percent: Int? = 0,
    var reason:String ?= "",
    val show:Boolean ?= false,
    val success:Boolean ?= false,
    val time:String ?= "",
    var tobe:String ?= "",
    var friends:String ?= "",
    val user:String ?= "",
    val imageUrl:String ?= "",
    val userVerification:Boolean ?= false

){

    var writeTime: Any = Any()

}