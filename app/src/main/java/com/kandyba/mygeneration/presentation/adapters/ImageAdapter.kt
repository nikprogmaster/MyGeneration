package com.kandyba.mygeneration.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.kandyba.mygeneration.R
import com.smarteist.autoimageslider.SliderViewAdapter
import com.squareup.picasso.Picasso


class ImageAdapter(private val imageURLs: List<String>) :
    SliderViewAdapter<ImageAdapter.ImageViewHolder>() {

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageURL = imageURLs[position]
        with(holder) {
            Picasso.get()
                .load(imageURL)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error_placeholder)
                .into(image)
        }
    }

    override fun getCount(): Int {
        return imageURLs.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?): ImageViewHolder {
        return ImageViewHolder(
            LayoutInflater.from(parent?.context).inflate(R.layout.image_item, parent, false)
        )
    }

    class ImageViewHolder(view: View) : SliderViewAdapter.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.image_item)
    }
}