package com.savage9ishere.tiwarimart.main_flow.ui.user.favorites.on_favorite_click

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.savage9ishere.tiwarimart.R
import com.savage9ishere.tiwarimart.authentication.AuthActivity
import com.savage9ishere.tiwarimart.checkout.CheckoutActivity
import com.savage9ishere.tiwarimart.databinding.LoadItemDataFragmentBinding
import com.savage9ishere.tiwarimart.fragment_container.particular_item.ItemPhotosAdapter
import com.savage9ishere.tiwarimart.fragment_container.particular_item.ReviewsAdapter
import com.savage9ishere.tiwarimart.main_flow.ui.cart.cart_items_database.CartItemsDatabase
import com.savage9ishere.tiwarimart.main_flow.ui.home.CartItems
import com.savage9ishere.tiwarimart.main_flow.ui.home.Item
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException

class LoadItemDataFragment : Fragment() {

    private lateinit var viewModel: LoadItemDataViewModel
    private lateinit var item : Item

    private var key : String? = null
    private var categoryName : String? = null
    private var itemName : String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = LoadItemDataFragmentBinding.inflate(inflater)

        try {
            key = requireArguments().getString("key")
            categoryName = requireArguments().getString("categoryName")
            itemName = requireArguments().getString("itemName")
        } catch (e: IllegalStateException){
            val bundle = requireActivity().intent.extras
            key = bundle!!.getString("key")
            categoryName = bundle.getString("categoryName")
            itemName = bundle.getString("itemName")
        }


        (activity as AppCompatActivity).supportActionBar?.title = itemName

        val application = requireNotNull(this.activity).application
        val cartDataSource = CartItemsDatabase.getInstance(application).cartItemDao
        val favoriteDataSource = CartItemsDatabase.getInstance(application).favoritesDao

        val viewModelFactory = LoadItemDataViewModelFactory(categoryName!!, key!!, cartDataSource, favoriteDataSource)
        viewModel = ViewModelProvider(this, viewModelFactory).get(LoadItemDataViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val itemPhotosAdapter = ItemPhotosAdapter()
        binding.itemPhotosRecyclerView.adapter = itemPhotosAdapter
        binding.itemPhotosRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

        val reviewsAdapter = ReviewsAdapter(requireActivity())
        binding.customerReviewsRecyclerView.adapter = reviewsAdapter
        binding.customerReviewsRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        binding.writeReviewButton.setOnClickListener {
            val b = Bundle()
            b.putParcelable("item", item)
            b.putString("category_name", categoryName)
            if (findNavController().currentDestination?.id == R.id.loadItemDataFragment)
                findNavController().navigate(R.id.action_loadItemDataFragment_to_reviewFragment2, b)
            else if (findNavController().currentDestination?.id == R.id.loadItemDataFragment2)
                findNavController().navigate(R.id.action_loadItemDataFragment2_to_reviewFragment3, b)
        }

        viewModel.thisItemPublic.observe(viewLifecycleOwner, {
            it?.let {
                item = it
            }
        })

        binding.buyNowButton.setOnClickListener {
            viewModel.moveAheadToBuyItem()
        }

        binding.addToCartButton.setOnClickListener {
            viewModel.addToCart()
        }

        binding.favoriteImage.setOnClickListener {
            viewModel.handleFavoriteClick()
        }

        viewModel.otherSizes.observe(viewLifecycleOwner, {
            it?.let {
                val sizes = it
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
            }
        })



        val qtySpinner = binding.qtySpinner
        val list = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20)
        val adapter2 = ArrayAdapter(requireContext(), R.layout.spinner_item, list)
        qtySpinner.adapter = adapter2
        qtySpinner.setSelection(0)

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

        viewModel.itemReviews.observe(viewLifecycleOwner, {
            it?.let {
                reviewsAdapter.submitList(it.toList())
            }
        })

        viewModel.itemCountInFavorite.observe(viewLifecycleOwner, {
            it?.let {
                if (it == 0){
                    binding.favoriteImage.setImageResource(R.drawable.ic_favorite_border)
                }
                else if (it == 1){
                    binding.favoriteImage.setImageResource(R.drawable.ic_favorites)
                }
            }
        })

        return binding.root
    }
}