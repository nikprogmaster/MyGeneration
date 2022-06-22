package com.kandyba.mygeneration.models.presentation

import com.kandyba.mygeneration.models.EMPTY_STRING
import com.kandyba.mygeneration.models.data.PostModel

private const val VIDEO = "video"
private const val PHOTO = "photo"

data class VkPost(
    val text: String,
    val likesCount: Int,
    val viewsCount: Int,
    val attachments: List<Attachment>
)

sealed class Attachment

data class Video(
    val frames: List<Frame>
) : Attachment()

data class Photo(
    val images: List<Image>
) : Attachment()

data class Image(
    val height: Int,
    val width: Int,
    val url: String,
    val type: String
)

data class Frame(
    val height: Int,
    val width: Int,
    val url: String
)

fun PostModel.toEntity(): VkPost {
    val videos = mutableListOf<Video>()
    val photos = mutableListOf<Photo>()
    attachments?.let { attach ->
        for (a in attach) {
            if (a.type == VIDEO) {
                a.video?.image?.let { images ->
                    videos.add(Video(images.map { im -> createFrame(im.height, im.width, im.url) }))
                }
            } else if (a.type == PHOTO) {
                a.photo?.sizes?.let { sizes ->
                    photos.add(Photo(sizes.map { im ->
                        createImage(
                            im?.height,
                            im?.width,
                            im?.url,
                            im?.type
                        )
                    }))
                }
            }
        }
    }
    return VkPost(
        text ?: EMPTY_STRING,
        likes?.count ?: 0,
        views?.count ?: 0,
        videos + photos
    )
}

private fun createImage(height: Int?, width: Int?, url: String?, type: String?) =
    Image(height ?: 0, width ?: 0, url.orEmpty(), type.orEmpty())

private fun createFrame(height: Int?, width: Int?, url: String?) =
    Frame(height ?: 0, width ?: 0, url.orEmpty())