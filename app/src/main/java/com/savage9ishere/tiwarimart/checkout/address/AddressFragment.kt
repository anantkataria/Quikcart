package com.savage9ishere.tiwarimart.checkout.address

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.savage9ishere.tiwarimart.R
import com.savage9ishere.tiwarimart.databinding.AddressFragmentBinding
import com.savage9ishere.tiwarimart.main_flow.ui.cart.cart_items_database.CartItemsDatabase
import com.savage9ishere.tiwarimart.main_flow.ui.home.AddressItem
import com.savage9ishere.tiwarimart.main_flow.ui.home.CartItems

class AddressFragment : Fragment() {

    private lateinit var viewModel: AddressViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = AddressFragmentBinding.inflate(inflater)

        (activity as AppCompatActivity).supportActionBar?.title = "Address"

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
        }) {
                viewModel.onEditAddressClicked(it)
            }
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
                val b = Bundle()
                val addressItem = AddressItem(it.fullName, it.mobileNumber, it.pinCode, it.flatHouseNoName, it.areaColonyStreet, it.landmark, it.townCity, it.state, it.deliveryInstructions)
                b.putParcelable("address", addressItem)
                b.putParcelableArrayList("itemsList", itemsList)
                if (findNavController().currentDestination?.id == R.id.addressFragment)
                    findNavController().navigate(R.id.action_addressFragment_to_paymentFragment, b)
                viewModel.doneOnDeliverToThisAddress()
            }
        })

        viewModel.onEditThisAddress.observe(viewLifecycleOwner, {
            it?.let {
                val b = Bundle()
                val addressItem = AddressItem(it.fullName, it.mobileNumber, it.pinCode, it.flatHouseNoName, it.areaColonyStreet, it.landmark, it.townCity, it.state, it.deliveryInstructions)
                val addressId = it.addressId
                b.putParcelable("addressItem", addressItem)
                b.putLong("addressId", addressId)
                if (findNavController().currentDestination?.id == R.id.addressFragment)
                    findNavController().navigate(R.id.action_addressFragment_to_editAddressFragment, b)
                viewModel.doneOnEditAddress()
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