package com.teamgether.willing.model

data class ChallengeInfo(
    var id : String = "",
    var title: String ?= "",
    var category: String ?= "",
    var count: Int = 0,
    var bank: String? = "",
    var account: Int = 0,
    var money: Int = 0,
    var email: String? = "",
    var per_week : Int = 0,
    var percent : Int = 0,
    var show : Boolean = true,
    var success : Boolean = true,
    var total_count : Int = 0,
    var total_week : Int = 0
)
