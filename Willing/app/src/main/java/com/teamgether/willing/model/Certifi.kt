package com.teamgether.willing.model

data class Certifi(
    var imgUrl : String? = "",
    var timestamp : String? = "",
    var challengeId : String? = "",
    var cheering : ArrayList<String>? = null,
    var question : ArrayList<String>? = null,
    var userName : String? = ""
)
