package com.savage9ishere.tiwarimart.fragment_container.particular_item

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.savage9ishere.tiwarimart.R
import com.savage9ishere.tiwarimart.databinding.ReviewPhotoListItemBinding

class ReviewPhotosAdapter : ListAdapter<String, ReviewPhotosAdapter.ViewHolder>(ReviewItemPhotosDiffCallback()) {
    class ViewHolder private constructor(val binding : ReviewPhotoListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private val itemImage: ImageView = binding.image

        fun bind(item: String) {
            Glide.with(itemImage.context)
                .load(item.toUri().buildUpon().scheme("https").build())
                .apply(
                    RequestOptions()
                    .placeholder(R.drawable.ic_secure))
                .into(itemImage)
        }

        companion object {
            fun from(parent: ViewGroup) : ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ReviewPhotoListItemBinding.inflate(layoutInflater)
                return ViewHolder(binding)
            }
        }
    }




    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder.from(parent)
    }

}

class ReviewItemPhotosDiffCallback : DiffUtil.ItemCallback<String>(){
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

}