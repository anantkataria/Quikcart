package com.savage9ishere.tiwarimart.checkout.final_bill

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.savage9ishere.tiwarimart.databinding.FinalBillFragmentBinding
import com.savage9ishere.tiwarimart.main_flow.MainActivity
import com.savage9ishere.tiwarimart.main_flow.ui.cart.cart_items_database.CartItemsDatabase
import com.savage9ishere.tiwarimart.main_flow.ui.home.AddressItem
import com.savage9ishere.tiwarimart.main_flow.ui.home.CartItems

class FinalBillFragment : Fragment() {

    private lateinit var viewModel: FinalBillViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FinalBillFragmentBinding.inflate(inflater)

        (activity as AppCompatActivity).supportActionBar?.title = "Checkout"


        val itemsList : ArrayList<CartItems>? = requireArguments().getParcelableArrayList("itemsList")
        val address : AddressItem? = requireArguments().getParcelable("address")
        val paymentMethod : String? = requireArguments().getString("paymentMethod")

        val application = requireNotNull(this.activity).application
        val cartDataSource = CartItemsDatabase.getInstance(application).cartItemDao

        val viewModelFactory = FinalBillViewModelFactory(itemsList!!, address!!, paymentMethod!!, cartDataSource)
        viewModel = ViewModelProvider(this, viewModelFactory).get(FinalBillViewModel::class.java)

        binding.viewModel = viewModel

        val adapter = FinalBillAdapter()
        binding.itemsRecyclerView.adapter = adapter
        binding.itemsRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        binding.placeOrderButton.setOnClickListener {
            viewModel.placeOrder()
        }

        viewModel.orderPlacedSuccessfully.observe(viewLifecycleOwner, {
            it?.let {
                if(it){
                    Toast.makeText(context, "Order Placed Successfully", Toast.LENGTH_LONG).show()
                    val intent = Intent(requireActivity(), MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
                else {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show()
                }
                viewModel.doneOrderPlaced()
            }
        })

        viewModel.cartItems.observe(viewLifecycleOwner, {
            it?.let {
                adapter.submitList(it)
            }
        })

        binding.lifecycleOwner = this

        return binding.root
    }

}