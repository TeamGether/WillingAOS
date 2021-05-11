package com.teamgether.willing.model

data class Friends (
    var profileImg: String? = "",
    var userName: String? = "",
    var email: String? = "",
        ) {
    override fun toString(): String {
        return "Friends(profileImg=$profileImg, userName=$userName, email=$email)"
    }
}