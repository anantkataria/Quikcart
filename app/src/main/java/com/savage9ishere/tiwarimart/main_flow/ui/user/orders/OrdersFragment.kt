package com.savage9ishere.tiwarimart.main_flow.ui.user.orders

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.savage9ishere.tiwarimart.R
import com.savage9ishere.tiwarimart.authentication.AuthActivity
import com.savage9ishere.tiwarimart.checkout.final_bill.OrderItem
import com.savage9ishere.tiwarimart.databinding.OrdersFragmentBinding
import com.savage9ishere.tiwarimart.main_flow.ui.cart.cart_items_database.CartItemsDatabase
import com.savage9ishere.tiwarimart.main_flow.ui.user.orders.previous_orders.PreviousOrderEntity
import com.savage9ishere.tiwarimart.main_flow.ui.user.orders.previous_orders.PreviousOrdersAdapter

class OrdersFragment : Fragment() {


    private lateinit var viewModel: OrdersViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = OrdersFragmentBinding.inflate(inflater)

        val application = requireNotNull(this.activity).application
        val previousOrdersDataSource = CartItemsDatabase.getInstance(application).previousOrderDao

        val viewModelFactory = OrdersViewModelFactory(previousOrdersDataSource)
        viewModel = ViewModelProvider(this, viewModelFactory).get(OrdersViewModel::class.java)

        binding.lifecycleOwner = this

        val currentOrderAdapter = CurrentOrderAdapter{
            currentOrderClick(it)
        }
        binding.currentOrdersRecyclerView.adapter = currentOrderAdapter
        binding.currentOrdersRecyclerView.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, true)

        viewModel.currentItems.observe(viewLifecycleOwner, {
            it?.let {
                currentOrderAdapter.submitList(it.toList())
            }
        })

        val previousOrderAdapter = PreviousOrdersAdapter {
            previousOrderClick(it)
        }
        binding.previousOrdersRecyclerView.adapter = previousOrderAdapter
        binding.previousOrdersRecyclerView.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)

        viewModel.previousOrderItems.observe(viewLifecycleOwner, {
            it?.let {
                previousOrderAdapter.submitList(it)
            }
        })

        viewModel.notLoggedIn.observe(viewLifecycleOwner, {
            it?.let {
                if (it){
                    binding.currentOrdersRecyclerView.visibility = View.GONE
                    binding.currentOrdersHardCoded.visibility = View.GONE
                    binding.previousOrdersHardCoded.visibility = View.GONE
                    binding.previousOrdersRecyclerView.visibility = View.GONE
                    binding.loginButton.visibility = View.VISIBLE
                    binding.loginText.visibility = View.VISIBLE
                }
                else {
                    binding.currentOrdersRecyclerView.visibility = View.VISIBLE
                    binding.currentOrdersHardCoded.visibility = View.VISIBLE
                    binding.previousOrdersHardCoded.visibility = View.VISIBLE
                    binding.previousOrdersRecyclerView.visibility = View.VISIBLE
                    binding.loginButton.visibility = View.GONE
                    binding.loginText.visibility = View.GONE
                }
                viewModel.doneNotLoggedIn()
            }
        })

        binding.loginButton.setOnClickListener {
            val b = Bundle()
            b.putBoolean("fromOrders", true)
            b.putBoolean("fromCart", false)
            b.putBoolean("fromDirect", false)
            b.putBoolean("fromProfile", false)
            b.putParcelableArrayList("itemsList", arrayListOf())
            val intent = Intent(this.activity, AuthActivity::class.java)
            intent.putExtras(b)
            findNavController().popBackStack()
            startActivity(intent)
        }

        return binding.root
    }

    private fun previousOrderClick(it: PreviousOrderEntity) {
        val b = Bundle()
        val orderItem = OrderItem(listItems = it.listItems, address = it.address, paymentMethod = it.paymentMethod, orderKey = it.orderKey, orderPlacedTime = it.orderPlacedTime, orderDeliveredOrCancelledTime = it.orderDeliveredOrCancelledTime, status = it.status)
        b.putParcelable("orderItem", orderItem)
        if (findNavController().currentDestination?.id == R.id.ordersFragment) {
            findNavController().navigate(
                R.id.action_ordersFragment_to_particularPreviousOrderFragment,
                b
            )
        }
    }

    private fun currentOrderClick(orderItem: OrderItem) {
        val b = Bundle()
        b.putParcelable("orderItem", orderItem)
        if (findNavController().currentDestination?.id == R.id.ordersFragment) {
            findNavController().navigate(
                R.id.action_ordersFragment_to_particularCurrentOrderFragment,
                b
            )
        }
    }

}