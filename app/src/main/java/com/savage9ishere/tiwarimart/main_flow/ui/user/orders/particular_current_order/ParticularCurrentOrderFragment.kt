package com.savage9ishere.tiwarimart.main_flow.ui.user.orders.particular_current_order

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.savage9ishere.tiwarimart.checkout.final_bill.FinalBillAdapter
import com.savage9ishere.tiwarimart.checkout.final_bill.OrderItem
import com.savage9ishere.tiwarimart.databinding.ParticularCurrentOrderFragmentBinding

class ParticularCurrentOrderFragment : Fragment() {

    private lateinit var viewModel: ParticularCurrentOrderViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = ParticularCurrentOrderFragmentBinding.inflate(inflater)

        val orderItem : OrderItem? = requireArguments().getParcelable("orderItem")

        val listItems = orderItem!!.listItems
        val address = orderItem.address
        val paymentMethod = orderItem.paymentMethod
        val orderPlacedTime = orderItem.orderPlacedTime
        val status = orderItem.status

        val viewModelFactory = ParticularCurrentOrderViewModelFactory(listItems, address, paymentMethod, orderPlacedTime, status)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ParticularCurrentOrderViewModel::class.java)

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

        binding.requestCancellationButton.setOnClickListener {
            viewModel.requestCancellation(orderItem)
        }

        viewModel.cancellationRequest.observe(viewLifecycleOwner, {
            it?.let {
                if (it){
                    Toast.makeText(context, "Cancellation requested", Toast.LENGTH_SHORT).show()
                    val s = "STATUS : CANCELLATION REQUESTED"
                    binding.orderStatusTextView.text = s
                }
                else {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
                viewModel.doneCancellationRequest()
            }
        })

        return binding.root
    }

}