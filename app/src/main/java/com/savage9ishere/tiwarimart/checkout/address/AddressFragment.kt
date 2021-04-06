package com.savage9ishere.tiwarimart.checkout.address

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.savage9ishere.tiwarimart.R
import com.savage9ishere.tiwarimart.databinding.AddressFragmentBinding
import com.savage9ishere.tiwarimart.main_flow.ui.cart.cart_items_database.CartItemsDatabase
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

        val application = requireActivity().application
        val addressDataSource = CartItemsDatabase.getInstance(application).addressDao

        val viewModelFactory = AddressViewModelFactory(addressDataSource)
        viewModel = ViewModelProvider(this, viewModelFactory).get(AddressViewModel::class.java)

        val addressAdapter =
            AddressAdapter({
                           viewModel.onRootClicked()
        }, {
           viewModel.onDeliverToThisAddressClicked(it)
        }, {
           viewModel.onEditAddressClicked(it)
        }, {
            viewModel.onAddDeliveryInstructionsClicked(it)
        })
        binding.addressRecyclerView.adapter = addressAdapter
        binding.addressRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        binding.addNewAddressButton.setOnClickListener {
            findNavController().navigate(R.id.action_addressFragment_to_addNewAddressFragment)
        }

        viewModel.addressItems.observe(viewLifecycleOwner, {
            it?.let {
                addressAdapter.submitList(it)
            }
        })

        viewModel.onRootClicked.observe(viewLifecycleOwner, {
            it?.let {
                addressAdapter.notifyDataSetChanged()
                viewModel.doneOnRootClicked()
            }
        })

        viewModel.onDeliverToThisAddress.observe(viewLifecycleOwner, {
            it?.let {
                Toast.makeText(context, "deliver to this address", Toast.LENGTH_SHORT).show()
                //todo
            }
        })

        viewModel.onEditThisAddress.observe(viewLifecycleOwner, {
            it?.let {
                Toast.makeText(context, "edit this address", Toast.LENGTH_SHORT).show()
                //todo
            }
        })

        viewModel.onAddDeliveryInstructions.observe(viewLifecycleOwner, {
            it?.let {
                Toast.makeText(context, "Add delivery instructions", Toast.LENGTH_SHORT).show()
                //todo
            }
        })

        return binding.root
    }

}