package com.teamgether.willing.model

data class ProfileInfo(
    val imageUrl: String? = "",
    val name: String = "",
    val email: String = "",
    val tobeTitleCount:Map<String,Int>,
    val challengeTitlePercent:Map<String,Int>,
    val followerCount: Int = 0,
    val followCount: Int =0
)