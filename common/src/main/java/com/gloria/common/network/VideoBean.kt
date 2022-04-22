package com.gloria.common.network

import com.google.gson.annotations.SerializedName

data class VideoBean(
        @SerializedName("_id") val id: String,
        @SerializedName("feedurl") val feedurl: String,
        @SerializedName("nickname") val nickname: String,
        @SerializedName("description") val description: String,
        @SerializedName("likecount") var likecount: String,
        @SerializedName("avatar") val avatar: String,
        @SerializedName("thumbnails") val thumbnails: String
) {
    fun setLikeInt(like: Int) {
        likecount = like.toString()
    }

    fun getLikeInt(): Int {
        return Integer.parseInt(likecount)
    }
}