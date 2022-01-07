package com.kandyba.mygeneration.models.data

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


data class PostModel (
    @SerializedName("id")
    @Expose
    var id: Int? = null,

    @SerializedName("from_id")
    @Expose
    var fromId: Int? = null,

    @SerializedName("owner_id")
    @Expose
    var ownerId: Int? = null,

    @SerializedName("date")
    @Expose
    var date: Int? = null,

    @SerializedName("marked_as_ads")
    @Expose
    var markedAsAds: Int? = null,

    @SerializedName("post_type")
    @Expose
    var postType: String? = null,

    @SerializedName("text")
    @Expose
    var text: String? = null,

    @SerializedName("is_pinned")
    @Expose
    var isPinned: Int? = null,

    @SerializedName("attachments")
    @Expose
    var attachments: List<Attachment>? = null,

    @SerializedName("post_source")
    @Expose
    var postSource: PostSource? = null,

    @SerializedName("comments")
    @Expose
    var comments: Comments? = null,

    @SerializedName("likes")
    @Expose
    var likes: Likes? = null,

    @SerializedName("reposts")
    @Expose
    var reposts: Reposts? = null,

    @SerializedName("views")
    @Expose
    var views: Views? = null,

    @SerializedName("donut")
    @Expose
    var donut: Donut? = null,

    @SerializedName("short_text_rate")
    @Expose
    var shortTextRate: Double? = null,

    @SerializedName("hash")
    @Expose
    var hash: String? = null
)

data class Attachment(
    @SerializedName("type")
    @Expose
    var type: String? = null,

    @SerializedName("video")
    @Expose
    var video: Video? = null,

    @SerializedName("photo")
    @Expose
    var photo: Photo? = null
)

data class Comments (
    @SerializedName("can_post")
    @Expose
    var canPost: Int? = null,

    @SerializedName("count")
    @Expose
    var count: Int? = null,

    @SerializedName("groups_can_post")
    @Expose
    var groupsCanPost: Boolean? = null
)

data class Donut (
    @SerializedName("is_donut")
    @Expose
    var isDonut: Boolean? = null
)

data class Image (
    @SerializedName("height")
    @Expose
    var height: Int? = null,

    @SerializedName("url")
    @Expose
    var url: String? = null,

    @SerializedName("width")
    @Expose
    var width: Int? = null,

    @SerializedName("with_padding")
    @Expose
    var withPadding: Int? = null
)

data class Likes (
    @SerializedName("can_like")
    @Expose
    var canLike: Int? = null,

    @SerializedName("count")
    @Expose
    var count: Int? = null,

    @SerializedName("user_likes")
    @Expose
    var userLikes: Int? = null,

    @SerializedName("can_publish")
    @Expose
    var canPublish: Int? = null
)

data class PostSource (
    @SerializedName("platform")
    @Expose
    var platform: String? = null,

    @SerializedName("type")
    @Expose
    var type: String? = null
)

data class Reposts (
    @SerializedName("count")
    @Expose
    var count: Int? = null,

    @SerializedName("user_reposted")
    @Expose
    var userReposted: Int? = null
)

data class Video (
    @SerializedName("access_key")
    @Expose
    var accessKey: String? = null,

    @SerializedName("can_comment")
    @Expose
    var canComment: Int? = null,

    @SerializedName("can_like")
    @Expose
    var canLike: Int? = null,

    @SerializedName("can_repost")
    @Expose
    var canRepost: Int? = null,

    @SerializedName("can_subscribe")
    @Expose
    var canSubscribe: Int? = null,

    @SerializedName("can_add_to_faves")
    @Expose
    var canAddToFaves: Int? = null,

    @SerializedName("can_add")
    @Expose
    var canAdd: Int? = null,

    @SerializedName("comments")
    @Expose
    var comments: Int? = null,

    @SerializedName("date")
    @Expose
    var date: Int? = null,

    @SerializedName("duration")
    @Expose
    var duration: Int? = null,

    @SerializedName("image")
    @Expose
    var image: List<Image>? = null,

    @SerializedName("width")
    @Expose
    var width: Int? = null,

    @SerializedName("height")
    @Expose
    var height: Int? = null,

    @SerializedName("id")
    @Expose
    var id: Int? = null,

    @SerializedName("owner_id")
    @Expose
    var ownerId: Int? = null,

    @SerializedName("title")
    @Expose
    var title: String? = null,

    @SerializedName("track_code")
    @Expose
    var trackCode: String? = null,

    @SerializedName("type")
    @Expose
    var type: String? = null,

    @SerializedName("views")
    @Expose
    var views: Int? = null,

    @SerializedName("content_restricted")
    @Expose
    var contentRestricted: Int? = null,

    @SerializedName("content_restricted_message")
    @Expose
    var contentRestrictedMessage: String? = null
)

data class Photo(

    @SerializedName("album_id")
    @Expose
    var albumId: Int? = null,

    @SerializedName("date")
    @Expose
    var date: Int? = null,

    @SerializedName("id")
    @Expose
    var id: Int? = null,

    @SerializedName("owner_id")
    @Expose
    var ownerId: Int? = null,

    @SerializedName("has_tags")
    @Expose
    var hasTags: Boolean? = null,

    @SerializedName("access_key")
    @Expose
    var accessKey: String? = null,

    @SerializedName("sizes")
    @Expose
    var sizes: List<Size?>? = null,

    @SerializedName("text")
    @Expose
    var text: String? = null,

    @SerializedName("user_id")
    @Expose
    var userId: Int? = null
)

data class Size (

    @SerializedName("height")
    @Expose
    var height: Int? = null,

    @SerializedName("url")
    @Expose
    var url: String? = null,

    @SerializedName("type")
    @Expose
    var type: String? = null,

    @SerializedName("width")
    @Expose
    var width: Int? = null
)

data class Views (
    @SerializedName("count")
    @Expose
    var count: Int? = null
)


