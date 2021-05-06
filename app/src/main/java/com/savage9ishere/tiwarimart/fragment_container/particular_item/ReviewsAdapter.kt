package com.savage9ishere.tiwarimart.fragment_container.particular_item

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.net.toUri
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.savage9ishere.tiwarimart.R
import com.savage9ishere.tiwarimart.databinding.ReviewListItemBinding
import com.savage9ishere.tiwarimart.main_flow.ui.home.Review
import kotlin.math.acos

class ReviewsAdapter(val activity : FragmentActivity) : ListAdapter<Review, ReviewsAdapter.ViewHolder>(ReviewsDiffCallback()) {
    class ViewHolder private constructor(val binding: ReviewListItemBinding,val activity: FragmentActivity): RecyclerView.ViewHolder(binding.root){
        private val userImage : ImageView = binding.userImage
        private val userNameText : TextView = binding.userNameText
        private val ratingBar : RatingBar = binding.ratingBar
        private val reviewTitleText : TextView = binding.reviewTitleText
        private val reviewText : TextView = binding.reviewText
        private val reviewPhotosRecyclerView : RecyclerView = binding.reviewPhotosRecyclerView

        private var currentItem : Review? = null

        fun bind(item : Review){
            currentItem = item

            Glide.with(userImage.context)
                .load(item.user.photoUrl.toUri().buildUpon().scheme("https").build())
                .placeholder(R.drawable.person_profile)
                .into(userImage)

            userNameText.text = item.user.name

            ratingBar.rating = item.ratingVal

            reviewTitleText.text = item.title

            reviewText.text = item.review

            if (item.photosUrl.size > 0){
                val adapter = ReviewPhotosAdapter()
                reviewPhotosRecyclerView.adapter = adapter
                reviewPhotosRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
                adapter.submitList(item.photosUrl.toList())
                reviewPhotosRecyclerView.visibility = View.VISIBLE
            }

        }

        companion object {
            fun from(parent: ViewGroup, activity: FragmentActivity): ViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ReviewListItemBinding.inflate(layoutInflater)
                return ViewHolder(binding, activity)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent, activity)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}

class ReviewsDiffCallback : DiffUtil.ItemCallback<Review>() {
    override fun areItemsTheSame(oldItem: Review, newItem: Review): Boolean {
        return oldItem.key == newItem.key
    }

    override fun areContentsTheSame(oldItem: Review, newItem: Review): Boolean {
        return oldItem == newItem
    }

}
