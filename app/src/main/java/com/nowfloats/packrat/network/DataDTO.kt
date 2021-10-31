package com.nowfloats.packrat.network

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class DataDTO : Serializable {
    @SerializedName("id")
    val id: String? = null

    @SerializedName("title")
    val title: String? = null

    @SerializedName("description")
    val description: Any? = null

    @SerializedName("datetime")
    val datetime = 0

    @SerializedName("type")
    val type: String? = null

    @SerializedName("animated")
    val isAnimated = false

    @SerializedName("width")
    val width = 0

    @SerializedName("height")
    val height = 0

    @SerializedName("size")
    val size = 0

    @SerializedName("views")
    val views = 0

    @SerializedName("bandwidth")
    val bandwidth = 0

    @SerializedName("vote")
    val vote: Any? = null

    @SerializedName("favorite")
    val isFavorite = false

    @SerializedName("nsfw")
    val nsfw: Any? = null

    @SerializedName("section")
    val section: Any? = null

    @SerializedName("account_url")
    val accountUrl: Any? = null

    @SerializedName("account_id")
    val accountId = 0

    @SerializedName("is_ad")
    val isIsAd = false

    @SerializedName("in_most_viral")
    val isInMostViral = false

    @SerializedName("has_sound")
    val isHasSound = false

    @SerializedName("tags")
    val tags: List<Any>? = null

    @SerializedName("ad_type")
    val adType = 0

    @SerializedName("ad_url")
    val adUrl: String? = null

    @SerializedName("edited")
    val edited: String? = null

    @SerializedName("in_gallery")
    val isInGallery = false

    @SerializedName("deletehash")
    val deletehash: String? = null

    @SerializedName("name")
    val name: String? = null

    @SerializedName("link")
    val link: String? = null
    override fun toString(): String {
        return "DataDTO{" +
                "id = '" + id + '\'' +
                ",title = '" + title + '\'' +
                ",description = '" + description + '\'' +
                ",datetime = '" + datetime + '\'' +
                ",type = '" + type + '\'' +
                ",animated = '" + isAnimated + '\'' +
                ",width = '" + width + '\'' +
                ",height = '" + height + '\'' +
                ",size = '" + size + '\'' +
                ",views = '" + views + '\'' +
                ",bandwidth = '" + bandwidth + '\'' +
                ",vote = '" + vote + '\'' +
                ",favorite = '" + isFavorite + '\'' +
                ",nsfw = '" + nsfw + '\'' +
                ",section = '" + section + '\'' +
                ",account_url = '" + accountUrl + '\'' +
                ",account_id = '" + accountId + '\'' +
                ",is_ad = '" + isIsAd + '\'' +
                ",in_most_viral = '" + isInMostViral + '\'' +
                ",has_sound = '" + isHasSound + '\'' +
                ",tags = '" + tags + '\'' +
                ",ad_type = '" + adType + '\'' +
                ",ad_url = '" + adUrl + '\'' +
                ",edited = '" + edited + '\'' +
                ",in_gallery = '" + isInGallery + '\'' +
                ",deletehash = '" + deletehash + '\'' +
                ",name = '" + name + '\'' +
                ",link = '" + link + '\'' +
                "}"
    }
}