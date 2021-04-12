package com.savage9ishere.tiwarimart.main_flow.ui.user.orders.previous_orders.particular_previous_order

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.savage9ishere.tiwarimart.checkout.final_bill.FinalBillAdapter
import com.savage9ishere.tiwarimart.checkout.final_bill.OrderItem
import com.savage9ishere.tiwarimart.databinding.ParticularPreviousOrderFragmentBinding

class ParticularPreviousOrderFragment : Fragment() {

    private lateinit var viewModel: ParticularPreviousOrderViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = ParticularPreviousOrderFragmentBinding.inflate(inflater)

        val orderItem : OrderItem? = requireArguments().getParcelable("orderItem")

        val listItems = orderItem!!.listItems
        val address = orderItem.address
        val paymentMethod = orderItem.paymentMethod
        val orderPlacedTime = orderItem.orderPlacedTime
        val orderDeliveredOrCancelled = orderItem.orderDeliveredOrCancelledTime
        val status = orderItem.status

        val viewModelFactory = ParticularPreviousOrderViewModelFactory(listItems, address, paymentMethod, orderPlacedTime, orderDeliveredOrCancelled, status)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ParticularPreviousOrderViewModel::class.java)

        binding.viewModel = viewModel

        val adapter = FinalBillAdapter()
        binding.itemsRecyclerView.adapter = adapter
        binding.itemsRecyclerView.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        binding.lifecycleOwner = this

        viewModel.cartItems.observe(viewLifecycleOwner, {
            it?.let {
                adapter.submitList(it)
            }
        })

        return binding.root
    }

}