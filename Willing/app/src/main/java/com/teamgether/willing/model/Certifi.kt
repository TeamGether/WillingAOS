package com.teamgether.willing.model

data class Certifi(
    var imgUrl : String? = "",
    var timestamp : Long? = 0,
    var challengeId : String? = "",
    var cheering : ArrayList<String>? = null,
    var question : ArrayList<String>? = null,
    var userName : String? = ""
)
