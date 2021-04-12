package com.savage9ishere.tiwarimart.main_flow.ui.user.orders.previous_orders

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.savage9ishere.tiwarimart.databinding.PreviousOrderItemBinding
import java.text.SimpleDateFormat
import java.util.*

class PreviousOrdersAdapter(
    private val onClick : (PreviousOrderEntity) -> Unit
) : ListAdapter<PreviousOrderEntity, PreviousOrdersAdapter.ViewHolder>(PreviousOrderDiff()){
    class ViewHolder private constructor(val binding: PreviousOrderItemBinding, onClick: (PreviousOrderEntity) -> Unit): RecyclerView.ViewHolder(binding.root){
        private val orderDeliveredOrCancelledTime : TextView = binding.orderDeliveredText
        private val orderItemsText : TextView = binding.orderItemsText
        private val orderTotalCostText : TextView = binding.orderTotalCostText
        private val orderStatusText : TextView = binding.orderStatusTextView

        private var currentItem : PreviousOrderEntity? = null

        init {
            binding.root.setOnClickListener {
                currentItem?.let{
                    onClick(it)
                }
            }
        }

        fun bind(item : PreviousOrderEntity){
            currentItem = item

            val date = Date(item.orderDeliveredOrCancelledTime)
            val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm")
            simpleDateFormat.timeZone = TimeZone.getTimeZone("Asia/Kolkata")

            var s = ""
            if(item.status == "ORDER DELIVERED"){
                s = "Order delivered : ${simpleDateFormat.format(date)}"
            }
            else if(item.status == "ORDER CANCELLED"){
                s = "Order cancelled : ${simpleDateFormat.format((date))}"
            }
            orderDeliveredOrCancelledTime.text = s

            var ss = ""
            var total = 0
            for(itm in item.listItems){
                ss += itm.name + "\n"
                total += itm.price.toInt()
            }
            orderItemsText.text = ss

            val totalS = "Total : ₹ $total"
            orderTotalCostText.text = totalS

            val status  = "Order Status : " + item.status
            orderStatusText.text = status
        }

        companion object {
            fun from(parent : ViewGroup, onClick: (PreviousOrderEntity) -> Unit): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = PreviousOrderItemBinding.inflate(layoutInflater)
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
        holder.bind(item)
    }
}

class PreviousOrderDiff : DiffUtil.ItemCallback<PreviousOrderEntity>(){
    override fun areItemsTheSame(
        oldItem: PreviousOrderEntity,
        newItem: PreviousOrderEntity
    ): Boolean {
        return oldItem.orderKey == newItem.orderKey
    }

    override fun areContentsTheSame(
        oldItem: PreviousOrderEntity,
        newItem: PreviousOrderEntity
    ): Boolean {
        return oldItem == newItem
    }

}
