package com.savage9ishere.tiwarimart.main_flow.ui.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.savage9ishere.tiwarimart.databinding.SaveForLaterItemBinding
import com.savage9ishere.tiwarimart.main_flow.ui.cart.save_for_later_database.SaveForLaterEntity

class SaveForLaterAdapter(
    private val onDeleteClick : (SaveForLaterEntity) -> Unit,
    private val onMoveToCartClick : (SaveForLaterEntity) -> Unit
): ListAdapter<SaveForLaterEntity, SaveForLaterAdapter.ViewHolder>(SaveForLaterDiffCallback()){
    class ViewHolder private constructor(
        val binding: SaveForLaterItemBinding,
        val onDeleteClick: (SaveForLaterEntity) -> Unit,
        val onMoveToCartClick: (SaveForLaterEntity) -> Unit
    ) : RecyclerView.ViewHolder(binding.root){
        private val itemImage: ImageView = binding.itemImage
        private val itemNameText: TextView = binding.itemNameTextView
        private val itemPriceText : TextView = binding.itemPriceTextView
        private val stockAvailabilityText : TextView = binding.stockAvailabilityTextView
        private val sizeText : TextView = binding.itemSizeTextView
        private val moveToCartButton : Button = binding.moveToCartButton
        private val deleteButton : Button = binding.deleteItemButton

        private var saveForLaterItem: SaveForLaterEntity? = null

        init {
            deleteButton.setOnClickListener {
                saveForLaterItem?.let{
                    onDeleteClick(it)
                }
            }

            moveToCartButton.setOnClickListener {
                saveForLaterItem?.let {
                    onMoveToCartClick(it)
                }
            }
        }

        fun bind(item: SaveForLaterEntity){
            saveForLaterItem = item

            Glide.with(itemImage.context)
                .load(item.photoUrl.toUri().buildUpon().scheme("https").build())
                .into(itemImage)

            itemNameText.text  = item.itemName
            itemPriceText.text = item.itemPrice.toString()

            if(item.stockAvailability){
                stockAvailabilityText.text = "In stock."
            }
            else {
                stockAvailabilityText.text = "Out of stock."
            }

            sizeText.text = item.itemSize
        }

        companion object {
            fun from(parent: ViewGroup,
                     onDeleteClick: (SaveForLaterEntity) -> Unit,
                     onMoveToCartClick: (SaveForLaterEntity) -> Unit
            ) : ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = SaveForLaterItemBinding.inflate(layoutInflater)
                return ViewHolder(binding, onDeleteClick, onMoveToCartClick)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder.from(parent, onDeleteClick, onMoveToCartClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}

class SaveForLaterDiffCallback: DiffUtil.ItemCallback<SaveForLaterEntity>() {
    override fun areItemsTheSame(
        oldItem: SaveForLaterEntity,
        newItem: SaveForLaterEntity
    ): Boolean {
        return oldItem.saveForLaterId == newItem.saveForLaterId
    }

    override fun areContentsTheSame(
        oldItem: SaveForLaterEntity,
        newItem: SaveForLaterEntity
    ): Boolean {
        return oldItem == newItem
    }

}
