package com.savage9ishere.tiwarimart.fragment_container.particular_item.review

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.savage9ishere.tiwarimart.databinding.ItemPhotoListItemBinding

class ItemPhotosAdapter(private val onLongClick: (position: Int) -> Unit) : ListAdapter<String, ItemPhotosAdapter.ViewHolder>(ItemPhotosDiffCallback()){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder.from(parent, onLongClick)
    }

    override fun onBindViewHolder(holder: ItemPhotosAdapter.ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ViewHolder private constructor(val binding: ItemPhotoListItemBinding, val onLongClick: (position: Int) -> Unit) : RecyclerView.ViewHolder(binding.root){
        private val itemImage: ImageView = binding.itemImage

        init {
            binding.root.setOnLongClickListener {
                onLongClick(adapterPosition)
                true
            }
        }

        fun bind(item : String){
            itemImage.setImageURI(item.toUri())
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup, onLongClick: (position: Int) -> Unit) : ViewHolder {
                val layoutInflater =  LayoutInflater.from(parent.context)
                val binding = ItemPhotoListItemBinding.inflate(layoutInflater)
                return ViewHolder(binding, onLongClick)
            }
        }
    }

    class ItemPhotosDiffCallback : DiffUtil.ItemCallback<String>(){
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

    }

}