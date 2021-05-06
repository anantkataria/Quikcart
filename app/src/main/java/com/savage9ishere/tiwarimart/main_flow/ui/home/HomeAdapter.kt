package com.savage9ishere.tiwarimart.main_flow.ui.home

import android.text.Layout
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.savage9ishere.tiwarimart.databinding.CategoryListItemBinding


class HomeAdapter(private val onClick: (Category) -> Unit) : ListAdapter<Category, HomeAdapter.ViewHolder>(CategoriesDiffCallback()){
    class ViewHolder private constructor(val binding: CategoryListItemBinding, val onClick: (Category) -> Unit) : RecyclerView.ViewHolder(binding.root){
        private val categoryImage : ImageView = binding.categoryImage
        private val categoryName : TextView = binding.categoryNameText
        private var currentCategory: Category? = null

        init {
            binding.root.setOnClickListener {
                currentCategory?.let {
                    onClick(it)
                }
            }
        }

        fun bind(item: Category){
            currentCategory = item
//            categoryName.text = item.name
            Glide.with(categoryImage.context)
                .load(item.uri.toUri().buildUpon().scheme("https").build())
                .into(categoryImage)

            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup, onClick: (Category) -> Unit) : ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CategoryListItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding, onClick)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeAdapter.ViewHolder {
        return ViewHolder.from(parent, onClick)
    }

    override fun onBindViewHolder(holder: HomeAdapter.ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}

class CategoriesDiffCallback : DiffUtil.ItemCallback<Category>(){
    override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem == newItem
    }

}
