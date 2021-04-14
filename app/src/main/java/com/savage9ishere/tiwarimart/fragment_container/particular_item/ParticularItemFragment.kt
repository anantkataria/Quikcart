package com.savage9ishere.tiwarimart.fragment_container.particular_item

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.savage9ishere.tiwarimart.R
import com.savage9ishere.tiwarimart.authentication.AuthActivity
import com.savage9ishere.tiwarimart.checkout.CheckoutActivity
import com.savage9ishere.tiwarimart.databinding.ParticularItemFragmentBinding
import com.savage9ishere.tiwarimart.main_flow.ui.cart.cart_items_database.CartItemsDatabase
import com.savage9ishere.tiwarimart.main_flow.ui.home.CartItems
import com.savage9ishere.tiwarimart.main_flow.ui.home.Item

class ParticularItemFragment : Fragment() {

    companion object {
        fun newInstance() = ParticularItemFragment()
    }

    private lateinit var viewModel: ParticularItemViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = ParticularItemFragmentBinding.inflate(inflater)

        val item : Item? = requireArguments().getParcelable("item")
        val categoryName : String? = requireArguments().getString("category_name")

        val application = requireNotNull(this.activity).application
        val cartDataSource = CartItemsDatabase.getInstance(application).cartItemDao

        val viewModelFactory = ParticularItemViewModelFactory(item!!, cartDataSource, categoryName!!)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ParticularItemViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val itemPhotosAdapter = ItemPhotosAdapter()
        binding.itemPhotosRecyclerView.adapter = itemPhotosAdapter
        binding.itemPhotosRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

        binding.writeReviewButton.setOnClickListener {
            val b = Bundle()
            b.putParcelable("item", item)
            b.putString("category_name", categoryName)
            findNavController().navigate(R.id.action_particularItemFragment_to_reviewFragment, b)
        }

        binding.buyNowButton.setOnClickListener {
            viewModel.moveAheadToBuyItem()
        }

        binding.addToCartButton.setOnClickListener {
            viewModel.addToCart()
        }

        val sizes = item.otherSizes.keys.toList()
        val sizeSpinner = binding.sizeSpinner
        val adapter = ArrayAdapter(requireContext(), R.layout.spinner_item, sizes)
        sizeSpinner.adapter = adapter
        sizeSpinner.setSelection(0)

        sizeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val size = parent?.getItemAtPosition(position).toString()
                viewModel.setNewSize(size)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

        val qtySpinner = binding.qtySpinner
        val list = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20)
        val adapter2 = ArrayAdapter(requireContext(), R.layout.spinner_item, list)
        qtySpinner.adapter = adapter2
        sizeSpinner.setSelection(0)

        qtySpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.setQuantity(parent?.getItemAtPosition(position).toString().toInt())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

        viewModel.moveAheadToBuy.observe(viewLifecycleOwner, {
            it?.let {
                val itemsList : ArrayList<CartItems> = arrayListOf()
                itemsList.add(it)

                val auth = Firebase.auth
                val user = auth.currentUser

                val b = Bundle()

                if(user.phoneNumber.isEmpty() || user.phoneNumber == null){
                    //user is not logged in
                    //send to loginOrRegisterFragment
                    b.putBoolean("fromCart", true)
                    b.putBoolean("fromDirect", false)
                    b.putBoolean("fromProfile", false)
                    b.putBoolean("fromOrders", false)
                    b.putParcelableArrayList("itemsList", itemsList)
                    val intent = Intent(this.activity, AuthActivity::class.java)
                    intent.putExtras(b)
                    startActivity(intent)
                }
                else {
                    //user is signed in
                    //send directly to checkout
                    b.putParcelableArrayList("itemsList", itemsList)
                    val intent = Intent(this.activity, CheckoutActivity::class.java)
                    intent.putExtras(b)
                    startActivity(intent)
                }
                viewModel.doneMovingAhead()
            }
        })

        viewModel.doneInserting.observe(viewLifecycleOwner, {
            it?.let {
                Toast.makeText(context, "Added to cart!", Toast.LENGTH_LONG).show()
                viewModel.doneDoneInserting()
            }
        })

        viewModel.itemImages.observe(viewLifecycleOwner, {
            it?.let {
                itemPhotosAdapter.submitList(it.toList())
            }
        })

        return binding.root
    }
}