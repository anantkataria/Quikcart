package com.savage9ishere.tiwarimart.main_flow.ui.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.savage9ishere.tiwarimart.databinding.CartListItemBinding
import com.savage9ishere.tiwarimart.main_flow.ui.cart.cart_items_database.CartItemEntity

class CartItemAdapter(
    private val onItemClick : (CartItemEntity) -> Unit,
    private val onIncrementClick: (CartItemEntity) -> Unit,
    private val onDecrementClick: (CartItemEntity) -> Unit,
    private val onSaveForLaterClick: (CartItemEntity) -> Unit,
    private val onDeleteClick: (CartItemEntity) -> Unit
): ListAdapter<CartItemEntity, CartItemAdapter.ViewHolder>(CartItemDiffCallback()) {
    class ViewHolder private constructor(
        val binding: CartListItemBinding,
        val onItemClick: (CartItemEntity) -> Unit,
        val onIncrementClick: (CartItemEntity) -> Unit,
        val onDecrementClick: (CartItemEntity) -> Unit,
        val onSaveForLaterClick: (CartItemEntity) -> Unit,
        val onDeleteClick: (CartItemEntity) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        private val itemImage : ImageView = binding.itemImage
        private val itemNameText : TextView = binding.itemNameTextView
        private val itemPriceText : TextView = binding.itemPriceTextView
        private val stockAvailabilityText : TextView = binding.stockAvailabilityTextView
        private val sizeTextView : TextView = binding.itemSizeTextView
        private val decrementImage : ImageButton = binding.decrementImageButton
        private val incrementImage : ImageButton = binding.incrementImageButton
        private val qtyCountText : TextView = binding.qtyCountTextView
        private val saveForLaterButton : Button = binding.saveForLaterButton
        private val deleteButton : Button = binding.deleteItemButton
        private var cartItem : CartItemEntity? = null

        init {
            binding.root.setOnClickListener {
                cartItem?.let {
                    onItemClick(it)
                }
            }

            decrementImage.setOnClickListener {
//                qtyCountText.text = (qtyCountText.text.toString().toInt() + 1).toString()
                cartItem?.let {
                    // set click listener from Fragment
                    //update the value stored in the database
                    onDecrementClick(it)
                    val qty = qtyCountText.text.toString().toInt() - 1
                    qtyCountText.text = qty.toString()
                }
            }

            incrementImage.setOnClickListener {
                cartItem?.let {
                    //set click listener from fragment
                    //update the value stored in the database
                    onIncrementClick(it)
                    val qty = qtyCountText.text.toString().toInt() + 1
                    qtyCountText.text = qty.toString()
                }
            }

            saveForLaterButton.setOnClickListener {
                cartItem?.let {
                    //set click listener from fragment
                    //delete this item from the database, store it in the save
                    //for later
                    onSaveForLaterClick(it)
                }
            }

            deleteButton.setOnClickListener {
                cartItem?.let{
                    onDeleteClick(it)
                }
            }

        }

        fun bind(item : CartItemEntity) {
            cartItem = item
            Glide.with(itemImage.context)
                .load(item.photoUrl.toUri().buildUpon().scheme("https").build())
                .into(itemImage)

            itemNameText.text = item.itemName
            val price = "₹ ${item.itemPrice}"
            itemPriceText.text = price

            if(item.stockAvailability){
                stockAvailabilityText.text = "In stock."
            }
            else {
                stockAvailabilityText.text = "Out of stock."
            }

            sizeTextView.text = item.itemSize

            val qty = item.itemQty
            qtyCountText.text = qty.toString()

        }

        companion object {
            fun from(parent: ViewGroup,
                     onItemClick: (CartItemEntity) -> Unit,
                     onIncrementClick: (CartItemEntity) -> Unit,
                     onDecrementClick: (CartItemEntity) -> Unit,
                     onSaveForLaterClick: (CartItemEntity) -> Unit,
                     onDeleteClick: (CartItemEntity) -> Unit
            ): ViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CartListItemBinding.inflate(layoutInflater)
                return ViewHolder(binding, onItemClick,onIncrementClick, onDecrementClick, onSaveForLaterClick, onDeleteClick)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent, onItemClick, onIncrementClick, onDecrementClick, onSaveForLaterClick, onDeleteClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}

class CartItemDiffCallback: DiffUtil.ItemCallback<CartItemEntity>() {
    override fun areItemsTheSame(oldItem: CartItemEntity, newItem: CartItemEntity): Boolean {
        return (oldItem.cartId == newItem.cartId)
    }

    override fun areContentsTheSame(oldItem: CartItemEntity, newItem: CartItemEntity): Boolean {
        return oldItem.itemQty == newItem.itemQty && oldItem == newItem
    }

}
