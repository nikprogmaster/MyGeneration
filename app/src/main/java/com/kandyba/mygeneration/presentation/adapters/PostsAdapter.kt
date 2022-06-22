package com.kandyba.mygeneration.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kandyba.mygeneration.R
import com.kandyba.mygeneration.models.presentation.Attachment
import com.kandyba.mygeneration.models.presentation.Photo
import com.kandyba.mygeneration.models.presentation.Video
import com.kandyba.mygeneration.models.presentation.VkPost
import com.smarteist.autoimageslider.SliderView

class PostsAdapter(
    private var postsList: List<VkPost>
) : RecyclerView.Adapter<PostViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.post_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bindViews(postsList[position])
    }

    override fun getItemCount(): Int {
        return postsList.size
    }

}

class PostViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    private val mainTextView: TextView = view.findViewById(R.id.main_text)
    private val photosGallery: SliderView = view.findViewById(R.id.photos_gallery)
    private val viewsNumber: TextView = view.findViewById(R.id.views_number)
    private val likesNumber: TextView = view.findViewById(R.id.likes_number)
    private val showFull: TextView = view.findViewById(R.id.show_full_text)

    fun bindViews(post: VkPost) {
        mainTextView.text = post.text
        showFull.setOnClickListener {
            if (mainTextView.maxLines == 1000) {
                mainTextView.maxLines = 5
                showFull.text = view.context.resources.getString(R.string.show_full)
            } else {
                mainTextView.maxLines = 1000
                showFull.text = view.context.resources.getString(R.string.roll_up)
            }
        }
        viewsNumber.text = post.viewsCount.toString()
        likesNumber.text = post.likesCount.toString()
        val imagesAdapter = ImageAdapter(getImageUrls(post.attachments))
        photosGallery.setSliderAdapter(imagesAdapter)
    }

    private fun getImageUrls(attachments: List<Attachment>): List<String> {
        return mutableListOf<String>().apply {
            for (at in attachments) {
                when (at) {
                    is Video -> addAll(
                        at.frames.filter { it.width == VIDEO_FRAME_REQUIRED_WIDTH }.map { it.url }
                    )
                    is Photo -> addAll(
                        at.images.filter { it.type == PHOTO_REQUIRED_TYPE }.map { it.url }
                    )
                }
            }
        }.toList()
    }

    companion object {
        private const val VIDEO_FRAME_REQUIRED_WIDTH = 800
        private const val PHOTO_REQUIRED_TYPE = "r"
    }
}

