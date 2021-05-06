package com.savage9ishere.tiwarimart.main_flow.ui.user.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.savage9ishere.tiwarimart.R
import com.savage9ishere.tiwarimart.authentication.AuthActivity
import com.savage9ishere.tiwarimart.checkout.address.address_database.AddressEntity
import com.savage9ishere.tiwarimart.databinding.ProfileFragmentBinding
import com.savage9ishere.tiwarimart.main_flow.ui.cart.cart_items_database.CartItemsDatabase
import com.savage9ishere.tiwarimart.main_flow.ui.home.AddressItem

const val REQUEST_IMAGE_GET = 1

class ProfileFragment : Fragment() {

    private lateinit var viewModel: ProfileViewModel

    private var imageUri: Uri? = null

    private lateinit var imageView: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = ProfileFragmentBinding.inflate(inflater)

        imageView = binding.profileImage

        val application = requireNotNull(this.activity).application
        val addressDataSource = CartItemsDatabase.getInstance(application).addressDao

        val viewModelFactory = ProfileViewModelFactory(addressDataSource)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ProfileViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val adapter = ProfileAddressAdapter({ onDeleteClick(it) }, { onClick(it) })
        binding.addressRecyclerView.adapter = adapter
        binding.addressRecyclerView.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)

        binding.addPhotoButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "image/*"
            }
            if (intent.resolveActivity(requireActivity().packageManager) != null) {
                startActivityForResult(intent, REQUEST_IMAGE_GET)
            }
        }

        binding.saveChangesButton.setOnClickListener {
            val name = binding.nameTextInputLayout.editText?.text.toString()
            val phone = binding.phoneTextInputLayout.editText?.text.toString()
            val email = binding.emailTextInputLayout.editText?.text.toString()

            if (name.isEmpty() || phone.isEmpty() || email.isEmpty()) {
                Toast.makeText(context, "something is empty", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.storeUserInDatabase(name, phone, email)
            }
        }

        viewModel.changesSaved.observe(viewLifecycleOwner, {
            it?.let {
                if (it) {
                    Toast.makeText(context, "Saved Successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
                viewModel.doneSavingChanges()
            }
        })

        viewModel.addresses.observe(viewLifecycleOwner, {
            it?.let {
                adapter.submitList(it)
            }
        })

        viewModel.notLoggedIn.observe(viewLifecycleOwner, {
            it?.let {
                if (it) {
                    binding.loginLayout.visibility = View.VISIBLE
                    binding.mainLayout.visibility = View.GONE
                } else {
                    binding.loginLayout.visibility = View.GONE
                    binding.mainLayout.visibility = View.VISIBLE
                }
                viewModel.doneNotLoggedIn()
            }
        })

        viewModel.photoUrl.observe(viewLifecycleOwner, {
            it?.let {
                if (it != "") {
                    imageUri = it.toUri()
                    Glide.with(binding.profileImage.context)
                        .load(it.toUri().buildUpon().scheme("https").build())
                        .into(binding.profileImage)
                } else if (it == "") {
                    binding.profileImage.setImageResource(R.drawable.person_profile)
                }
            }
        })

        viewModel.photoUrlSaved.observe(viewLifecycleOwner, {
            it?.let {
                if (it) {
                    Toast.makeText(context, "Photo Saved Successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
                viewModel.donePhotoUrlSaved()
            }
        })

        viewModel.photoUrlDeleted.observe(viewLifecycleOwner, {
            it?.let {
                if (it) {
                    Toast.makeText(context, "Photo Deleted Successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
                viewModel.donePhotoUrlDeleted()
            }
        })

        binding.removePhotoButton.setOnClickListener {
            imageUri?.let {
                viewModel.removeImageFromStorage(it)
            }
        }

        binding.addAddressButton.setOnClickListener {
            if (findNavController().currentDestination?.id == R.id.profileFragment) {
                findNavController().navigate(R.id.action_profileFragment_to_addNewAddressFragment2)
            }
        }

        binding.loginButton.setOnClickListener {
            val b = Bundle()
            b.putBoolean("fromOrders", false)
            b.putBoolean("fromCart", false)
            b.putBoolean("fromDirect", false)
            b.putBoolean("fromProfile", true)
            b.putParcelableArrayList("itemsList", arrayListOf())
            val intent = Intent(this.activity, AuthActivity::class.java)
            intent.putExtras(b)
            findNavController().popBackStack()
            startActivity(intent)
        }

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_GET && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                viewModel.addPhotoToDatabase(data.data)
            }
        }
    }

    private fun onClick(it: AddressEntity) {
        val b = Bundle()
        val addressItem = AddressItem(it.fullName, it.mobileNumber, it.pinCode, it.flatHouseNoName, it.areaColonyStreet, it.landmark, it.townCity, it.state, it.deliveryInstructions)
        val addressId = it.addressId
        b.putParcelable("addressItem", addressItem)
        b.putLong("addressId", addressId)

        if (findNavController().currentDestination?.id == R.id.profileFragment) {
            findNavController().navigate(R.id.action_profileFragment_to_editAddressFragment2, b)
        }
    }

    private fun onDeleteClick(addressEntity: AddressEntity) {
        viewModel.deleteThisAddress(addressEntity)
    }
}