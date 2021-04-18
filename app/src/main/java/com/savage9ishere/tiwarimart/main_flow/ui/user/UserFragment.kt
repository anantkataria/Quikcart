package com.savage9ishere.tiwarimart.main_flow.ui.user

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.savage9ishere.tiwarimart.R
import com.savage9ishere.tiwarimart.databinding.UserFragmentBinding

class UserFragment : Fragment() {

    private lateinit var viewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        val binding = UserFragmentBinding.inflate(inflater)

        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        val adapter = UserAdapter{
            onClick(it)
        }
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        binding.lifecycleOwner = this

        viewModel.listItemData.observe(viewLifecycleOwner, {
            it?.let {
                adapter.submitList(it.toList())
            }
        })

        return binding.root
    }

    private fun onClick(item: ListItem) {
        when(item.name){
            "Orders" -> {
                if (findNavController().currentDestination?.id == R.id.navigation_user){
                    findNavController().navigate(R.id.action_navigation_user_to_ordersFragment)
                }
            }

            "Feedback" -> {
                if (findNavController().currentDestination?.id == R.id.navigation_user) {
                    findNavController().navigate(R.id.action_navigation_user_to_feedbackFragment)
                }
            }

            "About" -> {
                if (findNavController().currentDestination?.id == R.id.navigation_user) {
                    findNavController().navigate(R.id.action_navigation_user_to_aboutFragment)
                }
            }

            "Profile" -> {
                if (findNavController().currentDestination?.id == R.id.navigation_user) {
                    findNavController().navigate(R.id.action_navigation_user_to_profileFragment)
                }
            }

            "Favorites" -> {
                if (findNavController().currentDestination?.id == R.id.navigation_user) {
                    findNavController().navigate(R.id.action_navigation_user_to_favoritesFragment)
                }
            }
        }
    }

}

