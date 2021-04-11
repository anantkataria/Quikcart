package com.savage9ishere.tiwarimart.main_flow.ui.user.about

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.savage9ishere.tiwarimart.databinding.AboutListItemBinding

class AboutAdapter() : ListAdapter<AboutItem, AboutAdapter.ViewHolder> (AboutDiffCallback()){
    class ViewHolder private constructor(val binding : AboutListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private val itemImage : ImageView = binding.itemImage
        private val itemName : TextView = binding.itemNameText

        fun bind(item : AboutItem){
            itemName.text = item.name

            Glide.with(itemImage.context)
                .load(item.photo.toUri().buildUpon().scheme("https").build())
                .into(itemImage)
        }

        companion object {
            fun from(parent : ViewGroup) : ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = AboutListItemBinding.inflate(layoutInflater)
                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}

class AboutDiffCallback : DiffUtil.ItemCallback<AboutItem>(){
    override fun areItemsTheSame(oldItem: AboutItem, newItem: AboutItem): Boolean {
        return oldItem.photo == newItem.photo
    }

    override fun areContentsTheSame(oldItem: AboutItem, newItem: AboutItem): Boolean {
        return oldItem == newItem
    }

}
