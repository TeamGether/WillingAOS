package com.teamgether.willing.model

class Comment (
    var imgId: String? = "",
    var profileImg: String? = "",
    var userName: String? = "",
    var content: String? = "",
    var timestamp: String? = "",
    var servertime: Long = 0
){
    override fun toString(): String {
        return "Comment(imgId=$imgId, profileImg=$profileImg, userName=$userName, content=$content, timestamp=$timestamp)"
    }
}