package com.teamgether.willing.model

data class UserInfo(
    val name: String = "",
    var email: String? = "",
    val donationName: String? = "",
    val tobe: String? = "",
    val imageUrl: String? = ""
)