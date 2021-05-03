package com.savage9ishere.tiwarimart.search.search_result

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.savage9ishere.tiwarimart.databinding.ItemListItemBinding
import com.savage9ishere.tiwarimart.search.search_database.CategoryWiseEntity

class SearchResultAdapter(private val onClick : (CategoryWiseEntity) -> Unit) : ListAdapter<CategoryWiseEntity, SearchResultAdapter.ViewHolder>(SearchResultDiffCallback()){
    class ViewHolder private constructor(val binding: ItemListItemBinding, val onClick: (CategoryWiseEntity) -> Unit) : RecyclerView.ViewHolder(binding.root){
        private val itemImage: ImageView = binding.productImageView
        private val itemName : TextView = binding.productNameTextView
        private val ratingCountText : TextView = binding.ratingCountTextView
        private val ratingBar : RatingBar = binding.ratingBar
        private val itemPrice : TextView = binding.priceTextView
        private val itemOriginalPrice: TextView = binding.originalPriceTextView
        private val saveAmountText: TextView = binding.saveAmountTextView

        private var currentItem : CategoryWiseEntity? = null

        init {
            binding.root.setOnClickListener {
                currentItem?.let{
                    onClick(it)
                }
            }
        }

        fun bind(item : CategoryWiseEntity){
            currentItem = item
            itemName.text = item.itemName
            binding.executePendingBindings()

            val originalPriceStr = "₹" + item.price
            itemOriginalPrice.text = originalPriceStr

            val totalRating = item.ratingTotal
            val peopleRatingCount = item.ratingCount.toFloat()
            if (totalRating.toInt() > 0){
                ratingCountText.text = (totalRating/peopleRatingCount).toString()
            }
            else {
                ratingCountText.text = "0"
            }

            if(peopleRatingCount.toInt() != 0){
                ratingBar.rating = (totalRating/peopleRatingCount)
            }

            val originalPrice = item.price.toInt()
            val discount = item.discount.toInt()
            val price = originalPrice - (originalPrice * discount / 100)
            itemPrice.text = "$price"

            val saveAmount =
                "Save ₹" + (originalPrice * discount / 100).toString() + " ($discount%)"
            saveAmountText.text = saveAmount

            Glide.with(itemImage.context)
                .load(item.imageUrl.toUri().buildUpon().scheme("https").build())
                .into(itemImage)
        }

        companion object {
            fun from(parent : ViewGroup, onClick: (CategoryWiseEntity) -> Unit) : ViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemListItemBinding.inflate(layoutInflater)
                return ViewHolder(binding, onClick)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder.from(parent, onClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}

class SearchResultDiffCallback : DiffUtil.ItemCallback<CategoryWiseEntity>() {
    override fun areItemsTheSame(
        oldItem: CategoryWiseEntity,
        newItem: CategoryWiseEntity
    ): Boolean {
        return oldItem.itemId == newItem.itemId
    }

    override fun areContentsTheSame(
        oldItem: CategoryWiseEntity,
        newItem: CategoryWiseEntity
    ): Boolean {
        return oldItem == newItem
    }

}
