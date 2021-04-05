package com.savage9ishere.tiwarimart.checkout.address

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.savage9ishere.tiwarimart.R
import com.savage9ishere.tiwarimart.databinding.AddressFragmentBinding
import com.savage9ishere.tiwarimart.main_flow.ui.home.CartItems

class AddressFragment : Fragment() {

    private lateinit var viewModel: AddressViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = AddressFragmentBinding.inflate(inflater)

        val bundle = requireActivity().intent.extras
        val itemsList : ArrayList<CartItems> = bundle!!.getParcelableArrayList("itemsList")!!

        var s = ""
        for (item in itemsList){
            s += item.name + ", "
        }

        Log.e("32", s)

        return binding.root
    }

}