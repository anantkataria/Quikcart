package com.savage9ishere.tiwarimart.main_flow.ui.cart

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.savage9ishere.tiwarimart.R
import com.savage9ishere.tiwarimart.authentication.AuthActivity
import com.savage9ishere.tiwarimart.checkout.CheckoutActivity
import com.savage9ishere.tiwarimart.databinding.FragmentCartBinding
import com.savage9ishere.tiwarimart.main_flow.ui.cart.cart_items_database.CartItemEntity
import com.savage9ishere.tiwarimart.main_flow.ui.cart.cart_items_database.CartItemsDatabase

class CartFragment : Fragment() {

    private lateinit var viewModel: CartViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        val binding = FragmentCartBinding.inflate(inflater)

        (activity as AppCompatActivity).supportActionBar?.title = "Cart"

        val application = requireNotNull(this.activity).application
        val cartDataSource = CartItemsDatabase.getInstance(application).cartItemDao
        val saveForLaterDataSource = CartItemsDatabase.getInstance(application).saveForLaterDao

        val viewModelFactory = CartViewModelFactory(cartDataSource, saveForLaterDataSource)
        viewModel =
                ViewModelProvider(this, viewModelFactory).get(CartViewModel::class.java)

        val cartAdapter = CartItemAdapter({
                                          onItemClick(it)
        },
            {
                viewModel.onIncrementClick(it)
        },
            {
                viewModel.onDecrementClick(it)
        },
            {
                viewModel.onSaveForLaterClick(it)
        },
            {
                viewModel.onDeleteClick(it)
            })
        binding.cartItemsRecyclerView.adapter = cartAdapter
        binding.cartItemsRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        val saveForLaterAdapter = SaveForLaterAdapter(
            {
                viewModel.onDeleteClick2(it)
            },
            {
                viewModel.onMoveToCartClick(it)
            }
        )
        binding.savedForLaterRecyclerView.adapter = saveForLaterAdapter
        binding.savedForLaterRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.proceedToBuyButton.setOnClickListener {
            viewModel.moveAheadToBuyItems()
        }

        viewModel.moveAheadToBuy.observe(viewLifecycleOwner, {
            it?.let {
                val auth = Firebase.auth
                val user = auth.currentUser

                val b = Bundle()

                if(user!!.phoneNumber == null || user.phoneNumber!!.isEmpty()){
                    //user is not logged in
                    //send to loginOrRegisterFragment
                    b.putBoolean("fromCart", true)
                    b.putBoolean("fromDirect", false)
                    b.putBoolean("fromProfile", false)
                    b.putBoolean("fromOrders", false)
                    b.putParcelableArrayList("itemsList", it)
                    val intent = Intent(this.activity, AuthActivity::class.java)
                    intent.putExtras(b)
                    startActivity(intent)
                }
                else {
                    Log.e("96", "checkout activity will be called")
                    //user is signed in
                    //send directly to checkout
                    b.putParcelableArrayList("itemsList", it)
                    val intent = Intent(this.activity, CheckoutActivity::class.java)
                    intent.putExtras(b)
                    startActivity(intent)
                }
                viewModel.doneMovingAhead()
            }
        })

        viewModel.itemCount.observe(viewLifecycleOwner, {
            it?.let {
                if(it > 0){
                    binding.subtotalText.visibility = View.VISIBLE
                    binding.proceedToBuyButton.visibility = View.VISIBLE
                    binding.cartItemsRecyclerView.visibility = View.VISIBLE
                }
                else {
                    binding.subtotalText.visibility = View.GONE
                    binding.proceedToBuyButton.visibility = View.GONE
                    binding.cartItemsRecyclerView.visibility = View.GONE
                }
            }
            if(it == null){
                binding.subtotalText.visibility = View.GONE
                binding.proceedToBuyButton.visibility = View.GONE
                binding.cartItemsRecyclerView.visibility = View.GONE
            }

        })

        viewModel.itemCountForSaveForLater.observe(viewLifecycleOwner, {
            it?.let {
                if(it > 0){
                    binding.saveForLaterText.visibility = View.VISIBLE
                    binding.savedForLaterRecyclerView.visibility = View.VISIBLE
                }
                else {
                    binding.saveForLaterText.visibility = View.GONE
                    binding.savedForLaterRecyclerView.visibility = View.GONE
                }
            }

        })

        viewModel.cartItems.observe(viewLifecycleOwner, Observer {
            it?.let {
                cartAdapter.submitList(it)
            }

        })

        viewModel.saveForLaterItems.observe(viewLifecycleOwner, {
            it?.let {
                saveForLaterAdapter.submitList(it)
            }
        })

        return binding.root

    }

    private fun onItemClick(it: CartItemEntity) {
        val b = Bundle()
        b.putString("key", it.itemKey)
        b.putString("categoryName", it.itemCategory)
        b.putString("itemName", it.itemName)
        findNavController().navigate(R.id.action_navigation_cart_to_loadItemDataFragment, b)
    }
}