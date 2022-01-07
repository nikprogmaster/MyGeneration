package com.kandyba.mygeneration.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.kandyba.mygeneration.R
import com.kandyba.mygeneration.models.data.Attachment
import com.kandyba.mygeneration.models.data.PostModel
import com.smarteist.autoimageslider.SliderView

class PostsAdapter(private val postsList: List<PostModel>) :
    RecyclerView.Adapter<PostsAdapter.PostViewHolder>() {

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


    class PostViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        private val mainTextView: TextView = view.findViewById(R.id.main_text)
        private val photosGallery: SliderView = view.findViewById(R.id.photos_gallery)
        private val viewsNumber: TextView = view.findViewById(R.id.views_number)
        private val likesNumber: TextView = view.findViewById(R.id.likes_number)
        private val showFull: TextView = view.findViewById(R.id.show_full_text)

        fun bindViews(post: PostModel) {
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
            viewsNumber.text = post.views?.let { it.count.toString() }
                ?: view.context.resources.getString(R.string.zero)
            likesNumber.text = post.likes?.let { it.count.toString() }
                ?: view.context.resources.getString(R.string.zero)
            val imagesAdapter = post.attachments?.let { ImageAdapter(getImageUrls(it)) }
            imagesAdapter?.let { photosGallery.setSliderAdapter(imagesAdapter) }
        }

        fun getImageUrls(attachments: List<Attachment>): List<String> {
            return mutableListOf<String>().apply {
                for (at in attachments) {
                    if (at.type == VIDEO_CONST) {
                        at.video?.image?.map {
                            if (it.width == VIDEO_FRAME_REQUIRED_WIDTH) {
                                it.url?.let { url -> this.add(url) }
                            }
                        }
                    } else if (at.type == PHOTO_CONST) {
                        at.photo?.sizes?.map {
                            if (it?.type == PHOTO_REQUIRED_TYPE) {
                                it.url?.let { url -> this.add(url) }
                            }
                        }
                    }
                }
            }
        }
    }

    companion object {
        private const val VIDEO_CONST = "video"
        private const val PHOTO_CONST = "photo"
        private const val SPAN_COUNT = 2
        private const val VIDEO_FRAME_REQUIRED_WIDTH = 800
        private const val PHOTO_REQUIRED_TYPE = "r"
    }
}