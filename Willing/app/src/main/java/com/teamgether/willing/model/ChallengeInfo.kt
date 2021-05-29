package com.teamgether.willing.model

data class ChallengeInfo(
    var UID : String? = "",
    var cntPerWeek : Int = 0,
    var didFinish : Boolean = false,
    var didSuccess : Boolean = false,
    var percent : Long = 0,
    var price : Int = 0,
    var show : Boolean = false,
    var subject : String? = "",
    var targetAccount : String? = "",
    var targetBank : String? = "",
    var term : Int = 0,
    var title : String? = "",
)
