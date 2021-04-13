package com.teamgether.willing.model

data class Ranking(
    var title: String? = "",

    var nickname_first: String? = "",
    val rank_first: Int? = 1,
    var profileUrl_first: String? = "",

    var nickname_second: String? = "",
    val rank_second: Int? = 1,
    var profileUrl_second: String? = "",

    var nickname_third: String? = "",
    val rank_third: Int? = 1,
    var profileUrl_third: String? = ""
) {
    override fun toString(): String {
        return "Ranking(title=$title, nickname_first=$nickname_first, rank_first=$rank_first, profileUrl_first=$profileUrl_first, nickname_second=$nickname_second, rank_second=$rank_second, profileUrl_second=$profileUrl_second, nickname_third=$nickname_third, rank_third=$rank_third, profileUrl_third=$profileUrl_third)"
    }
}