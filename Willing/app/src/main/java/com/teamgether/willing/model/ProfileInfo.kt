package com.teamgether.willing.model

data class ProfileInfo(
    var profileImg: String? = "",
    var name: String = "",
    var email: String = "",
    var tobeTitleCount:Map<String,Int> ,
    var challengeTitlePercent:Map<String,Int>,
    var followerCount: Int = 0,
    var followCount: Int =0,
    var followStatus:String = "",
    var isMine:Boolean = false
)
