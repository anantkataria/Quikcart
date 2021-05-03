package com.savage9ishere.tiwarimart.main_flow.ui.user.favorites

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Layout
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.ktx.storageMetadata
import com.savage9ishere.tiwarimart.R
import com.savage9ishere.tiwarimart.databinding.FavoritesFragmentBinding
import com.savage9ishere.tiwarimart.main_flow.ui.cart.cart_items_database.CartItemsDatabase
import com.savage9ishere.tiwarimart.main_flow.ui.home.CartItems
import com.savage9ishere.tiwarimart.main_flow.ui.user.favorites.favorites_database.FavoritesEntity

class FavoritesFragment : Fragment() {

    private lateinit var viewModel: FavoritesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FavoritesFragmentBinding.inflate(inflater)

        val application = requireActivity().application
        val favoritesDataSource = CartItemsDatabase.getInstance(application).favoritesDao

        val viewModelFactory = FavoritesViewModelFactory(favoritesDataSource)
        viewModel = ViewModelProvider(this, viewModelFactory).get(FavoritesViewModel::class.java)
        binding.lifecycleOwner = this

        val adapter = FavoritesAdapter{onClick(it)}
        binding.favoritesRecyclerView.adapter = adapter
        binding.favoritesRecyclerView.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)

        viewModel.favoriteItems.observe(viewLifecycleOwner, {
            it?.let {
                adapter.submitList(it)
            }
        })

        return binding.root
    }

    private fun onClick(favoritesEntity: FavoritesEntity) {
        val b = Bundle()
        b.putString("key", favoritesEntity.databaseKey)
        b.putString("categoryName", favoritesEntity.categoryName)
        b.putString("itemName", favoritesEntity.itemName)
        findNavController().navigate(R.id.action_favoritesFragment_to_loadItemDataFragment, b)
    }

}