package com.teamgether.willing.model

data class Trial(
    var imgId: String? = "",
    var profileImg: String? = "",
    var userName: String? = "",
    var content: String? = "",
    var timestamp: String? = "",
    var servertime: Long = 0,
    var cheeringCnt: Long = 0,
    var questionCnt: Long = 0
) {
    override fun toString(): String {
        return "Trial(imgId=$imgId, profileImg=$profileImg, userName=$userName, content=$content, timestamp=$timestamp, servertime=$servertime, cheeringCnt=$cheeringCnt, questionCnt=$questionCnt)"
    }
}
