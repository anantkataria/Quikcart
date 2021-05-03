package com.savage9ishere.tiwarimart.search.search_dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.savage9ishere.tiwarimart.databinding.SearchDialogItemBinding

class SearchDialogAdapter(private val onClick : (Int) -> Unit) : ListAdapter<String, SearchDialogAdapter.ViewHolder>(SearchDialogDiffCallback()){
    class ViewHolder private constructor(val binding: SearchDialogItemBinding, onClick: (Int) -> Unit) : RecyclerView.ViewHolder(binding.root){
        private val suggestionText : TextView = binding.suggestionsText

        private var currentItem : String? = null
        private var currentPosition : Int? = null

        init {
            binding.root.setOnClickListener {
                currentItem?.let {
                    onClick(currentPosition!!)
                }
            }
        }

        fun bind(item : String, position: Int) {
            currentItem = item
            currentPosition = position
            suggestionText.text = item
        }

        companion object {
            fun from(parent : ViewGroup, onClick: (Int) -> Unit) : ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = SearchDialogItemBinding.inflate(layoutInflater)
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
        holder.bind(item, position)
    }
}

class SearchDialogDiffCallback : DiffUtil.ItemCallback<String>(){
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

}
