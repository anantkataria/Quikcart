package com.savage9ishere.tiwarimart.checkout.address.new_address

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.get
import androidx.navigation.fragment.findNavController
import com.savage9ishere.tiwarimart.R
import com.savage9ishere.tiwarimart.checkout.address.address_database.AddressEntity
import com.savage9ishere.tiwarimart.databinding.AddNewAddressFragmentBinding
import com.savage9ishere.tiwarimart.main_flow.ui.cart.cart_items_database.CartItemsDatabase

class AddNewAddressFragment : Fragment() {


    private lateinit var viewModel: AddNewAddressViewModel
    private var state : String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = AddNewAddressFragmentBinding.inflate(inflater)

        val application = requireActivity().application
        val addressDataSource = CartItemsDatabase.getInstance(application).addressDao

        val viewModelFactory = AddNewAddressViewModelFactory(addressDataSource)
        viewModel = ViewModelProvider(this, viewModelFactory).get(AddNewAddressViewModel::class.java)

        binding.lifecycleOwner = this

        binding.addAddressButton.setOnClickListener {
            val fullName = binding.fullNameTextInput.editText?.text.toString()
            val mobileNumber = binding.mobileNumberTextInput.editText?.text.toString()
            val pinCode = binding.pinCodeTextInput.editText?.text.toString()
            val flatHouseNoName = binding.houseNoTextInput.editText?.text.toString()
            val areaColonyStreet = binding.areaColonyStreetTextInput.editText?.text.toString()
            val landMark = binding.landmarkTextInput.editText?.text.toString()
            val townCity = binding.cityTextInput.editText?.text.toString()

            if(fullName.isEmpty() || mobileNumber.isEmpty() || pinCode.isEmpty() || flatHouseNoName.isEmpty() || areaColonyStreet.isEmpty() || landMark.isEmpty() || townCity.isEmpty() || state!!.isEmpty()){
                Toast.makeText(context, "Something is Missing", Toast.LENGTH_SHORT).show()
            }
            else if(state == "Select State"){
                Toast.makeText(context, "Select state", Toast.LENGTH_SHORT).show()
            }
            else {
                val address = AddressEntity(fullName = fullName, mobileNumber = mobileNumber, pinCode = pinCode, flatHouseNoName = flatHouseNoName, areaColonyStreet = areaColonyStreet, landmark = landMark, townCity = townCity, state = state!!, deliveryInstructions = "None")
                viewModel.addAddress(address)
            }

        }

        val stateSpinner = binding.stateSpinner
        val list = listOf("Select State", "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chhattisgarh", "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jharkhand", "Karnataka", "Kerala", "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya", "Mizoram", "Nagaland", "Odisha", "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu", "Telangana", "Tripura", "Uttar Pradesh", "Uttarakhand", "West Bengal", "Andaman Nicobar", "Chandigarh", "Dadra Nagar Haveli", "Delhi", "Jammu Kashmir", "Ladakh", "LakshaDweep", "Puducherry")
        val stateAdapter = ArrayAdapter(requireContext(), R.layout.spinner_item, list)
        stateSpinner.adapter = stateAdapter
        stateSpinner.setSelection(0)

        stateSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                state = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

        viewModel.doneInserting.observe(viewLifecycleOwner, {
            it?.let {
                Toast.makeText(context, "Address Added successfully", Toast.LENGTH_SHORT).show()
                viewModel.doneDoneInserting()
                this.findNavController().popBackStack()
            }
        })

        return binding.root
    }

}