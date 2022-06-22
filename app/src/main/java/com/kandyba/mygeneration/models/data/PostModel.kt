package com.kandyba.mygeneration.models.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PostModel (
    @SerialName("id")
    var id: Int? = null,

    @SerialName("from_id")
    var fromId: Int? = null,

    @SerialName("owner_id")
    var ownerId: Int? = null,

    @SerialName("date")
    var date: Int? = null,

    @SerialName("marked_as_ads")
    var markedAsAds: Int? = null,

    @SerialName("post_type")
    var postType: String? = null,

    @SerialName("text")
    var text: String? = null,

    @SerialName("is_pinned")
    var isPinned: Int? = null,

    @SerialName("attachments")
    var attachments: List<Attachment>? = null,

    @SerialName("post_source")
    var postSource: PostSource? = null,

    @SerialName("comments")
    var comments: Comments? = null,

    @SerialName("likes")
    var likes: Likes? = null,

    @SerialName("reposts")
    var reposts: Reposts? = null,

    @SerialName("views")
    var views: Views? = null,

    @SerialName("donut")
    var donut: Donut? = null,

    @SerialName("short_text_rate")
    var shortTextRate: Double? = null,

    @SerialName("hash")
    var hash: String? = null
)

@Serializable
data class Attachment(
    @SerialName("type")
    var type: String? = null,

    @SerialName("video")
    var video: Video? = null,

    @SerialName("photo")
    var photo: Photo? = null
)

@Serializable
data class Comments (
    @SerialName("can_post")
    var canPost: Int? = null,

    @SerialName("count")
    var count: Int? = null,

    @SerialName("groups_can_post")
    var groupsCanPost: Boolean? = null
)

@Serializable
data class Donut (
    @SerialName("is_donut")
    var isDonut: Boolean? = null
)

@Serializable
data class Image (
    @SerialName("height")
    var height: Int? = null,

    @SerialName("url")
    var url: String? = null,

    @SerialName("width")
    var width: Int? = null,

    @SerialName("with_padding")
    var withPadding: Int? = null
)

@Serializable
data class Likes (
    @SerialName("can_like")
    var canLike: Int? = null,

    @SerialName("count")
    var count: Int? = null,

    @SerialName("user_likes")
    var userLikes: Int? = null,

    @SerialName("can_publish")
    var canPublish: Int? = null
)

@Serializable
data class PostSource (
    @SerialName("platform")
    var platform: String? = null,

    @SerialName("type")
    var type: String? = null
)

@Serializable
data class Reposts (
    @SerialName("count")
    var count: Int? = null,

    @SerialName("user_reposted")
    var userReposted: Int? = null
)

@Serializable
data class Video (
    @SerialName("access_key")
    var accessKey: String? = null,

    @SerialName("can_comment")
    var canComment: Int? = null,

    @SerialName("can_like")
    var canLike: Int? = null,

    @SerialName("can_repost")
    var canRepost: Int? = null,

    @SerialName("can_subscribe")
    var canSubscribe: Int? = null,

    @SerialName("can_add_to_faves")
    var canAddToFaves: Int? = null,

    @SerialName("can_add")
    var canAdd: Int? = null,

    @SerialName("comments")
    var comments: Int? = null,

    @SerialName("date")
    var date: Int? = null,

    @SerialName("duration")
    var duration: Int? = null,

    @SerialName("image")
    var image: List<Image>? = null,

    @SerialName("width")
    var width: Int? = null,

    @SerialName("height")
    var height: Int? = null,

    @SerialName("id")
    var id: Int? = null,

    @SerialName("owner_id")
    var ownerId: Int? = null,

    @SerialName("title")
    var title: String? = null,

    @SerialName("track_code")
    var trackCode: String? = null,

    @SerialName("type")
    var type: String? = null,

    @SerialName("views")
    var views: Int? = null,

    @SerialName("content_restricted")
    var contentRestricted: Int? = null,

    @SerialName("content_restricted_message")
    var contentRestrictedMessage: String? = null
)

@Serializable
data class Photo(

    @SerialName("album_id")
    var albumId: Int? = null,

    @SerialName("date")
    var date: Int? = null,

    @SerialName("id")
    var id: Int? = null,

    @SerialName("owner_id")
    var ownerId: Int? = null,

    @SerialName("has_tags")
    var hasTags: Boolean? = null,

    @SerialName("access_key")
    var accessKey: String? = null,

    @SerialName("sizes")
    var sizes: List<Size?>? = null,

    @SerialName("text")
    var text: String? = null,

    @SerialName("user_id")
    var userId: Int? = null
)

@Serializable
data class Size (

    @SerialName("height")
    var height: Int? = null,

    @SerialName("url")
    var url: String? = null,

    @SerialName("type")
    var type: String? = null,

    @SerialName("width")
    var width: Int? = null
)

@Serializable
data class Views (
    @SerialName("count")
    var count: Int? = null
)


