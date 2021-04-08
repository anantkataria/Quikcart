package com.savage9ishere.tiwarimart.main_flow.ui.user.orders

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.savage9ishere.tiwarimart.checkout.final_bill.OrderItem
import com.savage9ishere.tiwarimart.databinding.CurrentOrderItemBinding
import java.text.SimpleDateFormat
import java.util.*

class CurrentOrderAdapter(private val onClick : (OrderItem) -> Unit): ListAdapter<OrderItem, CurrentOrderAdapter.ViewHolder>(CurrentOrdersDiffCallback()){
    class ViewHolder private constructor(val binding: CurrentOrderItemBinding, onClick: (OrderItem) -> Unit): RecyclerView.ViewHolder(binding.root){
        private val orderPlacedTimeText : TextView = binding.orderPlacedTimeText
        private val orderItemsText : TextView = binding.orderItemsText
        private val orderTotalCostText : TextView = binding.orderTotalCostText

        private var currentItem : OrderItem? = null

        init {
            binding.root.setOnClickListener {
                currentItem?.let {
                    onClick(it)
                }
            }
        }

        fun bind(item : OrderItem){
            currentItem = item

            val date = Date(item.orderPlacedTime)
            val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm")
            simpleDateFormat.timeZone = TimeZone.getTimeZone("Asia/Kolkata")

            val s = "Order placed : ${simpleDateFormat.format(date)}"
            orderPlacedTimeText.text = s

            var ss = ""
            var total = 0
            for(itm in item.listItems){
                ss += itm.name + "\n"
                total += itm.price.toInt()
            }
            orderItemsText.text = ss

            val totalS = "Total : ₹ $total"
            orderTotalCostText.text = totalS
        }

        companion object {
            fun from(parent: ViewGroup, onClick: (OrderItem) -> Unit): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CurrentOrderItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding, onClick)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CurrentOrderAdapter.ViewHolder {
        return ViewHolder.from(parent, onClick)
    }

    override fun onBindViewHolder(holder: CurrentOrderAdapter.ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}

class CurrentOrdersDiffCallback: DiffUtil.ItemCallback<OrderItem>() {
    override fun areItemsTheSame(oldItem: OrderItem, newItem: OrderItem): Boolean {
        return oldItem.orderKey == newItem.orderKey
    }

    override fun areContentsTheSame(oldItem: OrderItem, newItem: OrderItem): Boolean {
        return oldItem == newItem
    }

}
