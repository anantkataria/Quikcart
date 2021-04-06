package com.savage9ishere.tiwarimart.checkout.final_bill

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.savage9ishere.tiwarimart.databinding.FinalBillFragmentBinding
import com.savage9ishere.tiwarimart.main_flow.ui.home.AddressItem
import com.savage9ishere.tiwarimart.main_flow.ui.home.CartItems

class FinalBillFragment : Fragment() {

    private lateinit var viewModel: FinalBillViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FinalBillFragmentBinding.inflate(inflater)

        val itemsList : ArrayList<CartItems>? = requireArguments().getParcelableArrayList("itemsList")
        val address : AddressItem? = requireArguments().getParcelable("address")
        val paymentMethod : String? = requireArguments().getString("paymentMethod")

        val viewModelFactory = FinalBillViewModelFactory(itemsList!!, address!!, paymentMethod!!)
        viewModel = ViewModelProvider(this, viewModelFactory).get(FinalBillViewModel::class.java)

        binding.placeOrderButton.setOnClickListener {
            //todo
        }

        return binding.root
    }

}