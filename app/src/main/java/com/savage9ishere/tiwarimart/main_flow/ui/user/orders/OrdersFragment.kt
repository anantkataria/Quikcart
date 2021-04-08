package com.savage9ishere.tiwarimart.main_flow.ui.user.orders

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.savage9ishere.tiwarimart.R
import com.savage9ishere.tiwarimart.databinding.OrdersFragmentBinding

class OrdersFragment : Fragment() {


    private lateinit var viewModel: OrdersViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = OrdersFragmentBinding.inflate(inflater)

        viewModel = ViewModelProvider(this).get(OrdersViewModel::class.java)

        binding.lifecycleOwner = this

        val currentOrderAdapter = CurrentOrderAdapter{
            currentOrderClick()
        }
        binding.currentOrdersRecyclerView.adapter = currentOrderAdapter
        binding.currentOrdersRecyclerView.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, true)

        viewModel.currentItems.observe(viewLifecycleOwner, {
            it?.let {
                currentOrderAdapter.submitList(it.toList())
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

        return binding.root
    }

    private fun currentOrderClick() {
        Toast.makeText(context, "current item clicked", Toast.LENGTH_SHORT).show()
    }

}