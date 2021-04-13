package com.savage9ishere.tiwarimart.main_flow.ui.user.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.savage9ishere.tiwarimart.checkout.address.address_database.AddressEntity
import com.savage9ishere.tiwarimart.databinding.AddressListItem2Binding

class ProfileAddressAdapter(private val onDeleteClick: (AddressEntity) -> Unit, private val onClick: (AddressEntity) -> Unit) : ListAdapter<AddressEntity, ProfileAddressAdapter.ViewHolder> (ProfileAddressDiffCallback()){
    class ViewHolder private constructor(val binding : AddressListItem2Binding, onDeleteClick: (AddressEntity) -> Unit, onClick: (AddressEntity) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        private val addressText : TextView = binding.addressTextView
        private val deleteImage : ImageView = binding.deleteImage

        private var currentItem : AddressEntity? = null
        init {
            binding.root.setOnClickListener {
                currentItem?.let{
                    onClick(it)
                }
            }

            deleteImage.setOnClickListener {
                currentItem?.let{
                    onDeleteClick(it)
                }
            }
        }

        fun bind(item : AddressEntity){
            currentItem = item

            val address = item.fullName + "\n" + item.flatHouseNoName + ", " + item.areaColonyStreet + "\n" + item.landmark + " " + item.townCity + ", " + item.pinCode + "\n" + item.state
            addressText.text = address
        }

        companion object {
            fun from(parent: ViewGroup, onDeleteClick: (AddressEntity) -> Unit, onClick: (AddressEntity) -> Unit) : ViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = AddressListItem2Binding.inflate(layoutInflater)
                return ViewHolder(binding, onDeleteClick, onClick)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder.from(parent, onDeleteClick, onClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}

class ProfileAddressDiffCallback: DiffUtil.ItemCallback<AddressEntity>() {
    override fun areItemsTheSame(oldItem: AddressEntity, newItem: AddressEntity): Boolean {
        return oldItem.addressId == newItem.addressId
    }

    override fun areContentsTheSame(oldItem: AddressEntity, newItem: AddressEntity): Boolean {
        return oldItem == newItem
    }

}
