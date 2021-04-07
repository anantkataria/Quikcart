package com.savage9ishere.tiwarimart.fragment_container.particular_category

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
import com.savage9ishere.tiwarimart.main_flow.ui.home.Item
import kotlin.math.absoluteValue

class ItemsAdapter(private val onClick : (Item) -> Unit) : ListAdapter<Item, ItemsAdapter.ViewHolder>(ItemsDiffCallback()){
    class ViewHolder private constructor(val binding: ItemListItemBinding, val onClick: (Item) -> Unit): RecyclerView.ViewHolder(binding.root){
        private val itemImage: ImageView = binding.productImageView
        private val itemName : TextView = binding.productNameTextView
        private val ratingCountText : TextView = binding.ratingCountTextView
        private val ratingBar : RatingBar = binding.ratingBar
        private val itemPrice : TextView = binding.priceTextView
        private val itemOriginalPrice: TextView = binding.originalPriceTextView
        private val saveAmountText: TextView = binding.saveAmountTextView
        private val deliveryTimeApproxText: TextView = binding.deliveryTimeApproximateTextView

        private var currentItem: Item? = null

        init {
            binding.root.setOnClickListener {
                currentItem?.let{
                    onClick(it)
                }
            }
        }

        fun bind(item: Item) {
            currentItem = item
            val itemNameStr = item.name + " " + item.size
            itemName.text = itemNameStr


            binding.executePendingBindings()

            val originalPriceStr = "₹" + item.price
            itemOriginalPrice.text = originalPriceStr

            val totalRating = item.ratingTotal.toFloat()
            val peopleRatingCount = item.peopleRatingCount.toFloat()
            ratingCountText.text = totalRating.toString()
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

            val str = "Delivery in " + item.deliveryDuration
            deliveryTimeApproxText.text = str

            Glide.with(itemImage.context)
                .load(item.photosUrl[0].toUri().buildUpon().scheme("https").build())
                .into(itemImage)
        }

        companion object {
            fun from(
                parent: ViewGroup,
                onClick: (Item) -> Unit
            ): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemListItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding, onClick)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsAdapter.ViewHolder {
        return ViewHolder.from(parent, onClick)
    }

    override fun onBindViewHolder(holder: ItemsAdapter.ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }


}

class ItemsDiffCallback : DiffUtil.ItemCallback<Item>() {
    override fun areItemsTheSame(
        oldItem: Item,
        newItem: Item
    ): Boolean {
        return oldItem.key == newItem.key
    }

    override fun areContentsTheSame(
        oldItem: Item,
        newItem: Item
    ): Boolean {
        return oldItem == newItem
    }

}