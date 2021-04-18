package com.savage9ishere.tiwarimart.main_flow.ui.user.favorites


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
import com.savage9ishere.tiwarimart.main_flow.ui.user.favorites.favorites_database.FavoritesEntity

class FavoritesAdapter(private val onClick : (FavoritesEntity) -> Unit) : ListAdapter<FavoritesEntity, FavoritesAdapter.ViewHolder>(FavoritesDiffCallback()){
    class ViewHolder private constructor(val binding: ItemListItemBinding, val onClick: (FavoritesEntity) -> Unit): RecyclerView.ViewHolder(binding.root) {
        private val itemImage : ImageView = binding.productImageView
        private val itemName : TextView = binding.productNameTextView
        private val ratingCountText : TextView = binding.ratingCountTextView
        private val ratingBar : RatingBar = binding.ratingBar
        private val itemPrice : TextView = binding.priceTextView
        private val itemOriginalPrice: TextView = binding.originalPriceTextView
        private val saveAmountText: TextView = binding.saveAmountTextView
        private val deliveryTimeApproxText: TextView = binding.deliveryTimeApproximateTextView

        private var currentItem : FavoritesEntity? = null

        init {
            binding.root.setOnClickListener {
                currentItem?.let {
                    onClick(it)
                }
            }
        }

        fun bind(item : FavoritesEntity){
            currentItem = item
            val itemNameStr = item.itemName + " " + item.size
            itemName.text = itemNameStr


            binding.executePendingBindings()

            val originalPriceStr = "₹" + item.price
            itemOriginalPrice.text = originalPriceStr

            val totalRating = item.ratingTotal
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
                .load(item.photoUrl.toUri().buildUpon().scheme("https").build())
                .into(itemImage)
        }

        companion object {
            fun from(
                parent: ViewGroup,
                onClick: (FavoritesEntity) -> Unit
            ): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemListItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding, onClick)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent, onClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

}

class FavoritesDiffCallback: DiffUtil.ItemCallback<FavoritesEntity>() {
    override fun areItemsTheSame(oldItem: FavoritesEntity, newItem: FavoritesEntity): Boolean {
        return oldItem.databaseKey == newItem.databaseKey
    }

    override fun areContentsTheSame(oldItem: FavoritesEntity, newItem: FavoritesEntity): Boolean {
        return oldItem == newItem
    }

}