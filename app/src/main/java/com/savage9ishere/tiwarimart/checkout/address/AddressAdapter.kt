package com.savage9ishere.tiwarimart.checkout.address

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.savage9ishere.tiwarimart.checkout.address.address_database.AddressEntity
import com.savage9ishere.tiwarimart.databinding.AddressListItemBinding

class AddressAdapter(private val onRootClick: () -> Unit, private val onDeliverToThisAddress: (AddressEntity) -> Unit, private val onEditAddress: (AddressEntity)-> Unit, private val onAddDeliveryInstructions: (AddressEntity) -> Unit): ListAdapter<AddressEntity, AddressAdapter.ViewHolder>(AddressDiffCallback()) {
    class ViewHolder private constructor(val binding: AddressListItemBinding, val onRootClick: () -> Unit, val onDeliverToThisAddress: (AddressEntity) -> Unit, onEditAddress: (AddressEntity) -> Unit, onAddDeliveryInstructions: (AddressEntity) -> Unit): RecyclerView.ViewHolder(binding.root) {
        private val addressText: TextView = binding.addressTextView
        private val deliverToThisAddressButton : Button = binding.deliverToThisAddressButton
        private val editAddressText : TextView = binding.editAddressButton
        private val addDeliveryInstructions : TextView = binding.deliveryInstructionsButton

        private var currentAddress : AddressEntity? = null

        init {

            deliverToThisAddressButton.setOnClickListener {
                currentAddress?.let{
                    onDeliverToThisAddress(it)
                }
            }

            editAddressText.setOnClickListener {
                currentAddress?.let {
                    onEditAddress(it)
                }
            }

            addDeliveryInstructions.setOnClickListener {
                currentAddress?.let {
                    onAddDeliveryInstructions(it)
                }
            }
        }

        fun bind(item: AddressEntity){
            currentAddress = item

            val address = item.fullName + "\n" + item.flatHouseNoName + ", " + item.areaColonyStreet + "\n" + item.landmark + " " + item.townCity + ", " + item.pinCode + "\n" + item.state
            addressText.text = address

            binding.root.setOnClickListener {
                currentAddress?.let {
                    onRootClick()
                    deliverToThisAddressButton.visibility = View.VISIBLE
                    editAddressText.visibility = View.VISIBLE
                    addDeliveryInstructions.visibility = View.VISIBLE
                }
            }

//            deliverToThisAddressButton.visibility = View.GONE
//            editAddressText.visibility = View.GONE
//            addDeliveryInstructions.visibility = View.GONE
        }

        companion object {
            fun from(parent: ViewGroup, onRootClick: () -> Unit, onDeliverToThisAddress: (AddressEntity) -> Unit, onEditAddress: (AddressEntity) -> Unit, onAddDeliveryInstructions: (AddressEntity) -> Unit): ViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = AddressListItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding, onRootClick, onDeliverToThisAddress, onEditAddress, onAddDeliveryInstructions)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent, onRootClick, onDeliverToThisAddress, onEditAddress, onAddDeliveryInstructions)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}

class AddressDiffCallback : DiffUtil.ItemCallback<AddressEntity>() {
    override fun areItemsTheSame(oldItem: AddressEntity, newItem: AddressEntity): Boolean {
        return oldItem.addressId == newItem.addressId
    }

    override fun areContentsTheSame(oldItem: AddressEntity, newItem: AddressEntity): Boolean {
        return oldItem == newItem
    }

}
