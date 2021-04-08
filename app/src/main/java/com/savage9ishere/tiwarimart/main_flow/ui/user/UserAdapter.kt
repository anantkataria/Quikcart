package com.savage9ishere.tiwarimart.main_flow.ui.user

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.savage9ishere.tiwarimart.databinding.UserListItemBinding

class UserAdapter(private val onClick: (ListItem) -> Unit): ListAdapter<ListItem, UserAdapter.ViewHolder> (UserDiffCallback()) {
    class ViewHolder private constructor(val binding: UserListItemBinding, onClick: (ListItem) -> Unit): RecyclerView.ViewHolder(binding.root) {
        private val itemImage : ImageView = binding.img
        private val itemName : TextView = binding.nameTxt
        private val itemDescription : TextView = binding.descriptionText

        private var currentItem : ListItem? = null

        init {
            binding.root.setOnClickListener {
                currentItem?.let {
                    onClick(it)
                }
            }
        }

        fun bind(item : ListItem){
            currentItem = item

            itemImage.setImageResource(item.image)
            itemName.text = item.name
            itemDescription.text = item.description
        }

        companion object {
            fun from(parent : ViewGroup, onClick: (ListItem) -> Unit): ViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = UserListItemBinding.inflate(layoutInflater, parent, false)
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

class UserDiffCallback : DiffUtil.ItemCallback<ListItem>(){
    override fun areItemsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
        return oldItem.image == newItem.image
    }

    override fun areContentsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
        return oldItem == newItem
    }

}
