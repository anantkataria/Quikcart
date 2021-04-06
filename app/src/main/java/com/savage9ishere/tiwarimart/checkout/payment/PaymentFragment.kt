package com.savage9ishere.tiwarimart.checkout.payment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.savage9ishere.tiwarimart.R
import com.savage9ishere.tiwarimart.databinding.PaymentFragmentBinding
import com.savage9ishere.tiwarimart.main_flow.ui.home.AddressItem
import com.savage9ishere.tiwarimart.main_flow.ui.home.CartItems

class PaymentFragment : Fragment() {

    private lateinit var viewModel: PaymentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = PaymentFragmentBinding.inflate(inflater)

        val itemsList : ArrayList<CartItems>? = requireArguments().getParcelableArrayList("itemsList")
        val address : AddressItem? = requireArguments().getParcelable("address")

        viewModel = ViewModelProvider(this).get(PaymentViewModel::class.java)

        binding.codButton.setOnClickListener {
            val b = Bundle()
            b.putParcelableArrayList("itemsList", itemsList)
            b.putParcelable("address", address)
            b.putString("paymentMethod", "cod")
            findNavController().navigate(R.id.action_paymentFragment_to_finalBillFragment, b)
        }

        binding.upiButton.setOnClickListener {
//            val b = Bundle()
//            b.putParcelableArrayList("itemsList", itemsList)
//            b.putParcelable("address", address)
//            b.putString("paymentMethod", "upi")
//            findNavController().navigate(R.id.action_paymentFragment_to_finalBillFragment, b)
        }

        return binding.root
    }

}