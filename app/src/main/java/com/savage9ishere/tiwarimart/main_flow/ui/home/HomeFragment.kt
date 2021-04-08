package com.savage9ishere.tiwarimart.main_flow.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.savage9ishere.tiwarimart.R
import com.savage9ishere.tiwarimart.databinding.FragmentHomeBinding
import com.savage9ishere.tiwarimart.fragment_container.FragmentContainerActivity

//this fragment will contain all categories
class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        val binding: FragmentHomeBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_home, container, false
        )

        val adapter = HomeAdapter{this.gridViewItemOnClick(it)}
        binding.recyclerViewCategories.adapter = adapter

        val manager = GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
        binding.recyclerViewCategories.layoutManager = manager

        binding.lifecycleOwner =this
        binding.viewmodel = homeViewModel

        homeViewModel.categories.observe(viewLifecycleOwner, {
            it?.let {
                adapter.submitList(it.toList())
            }
        })

        return binding.root
    }

    private fun gridViewItemOnClick(category: Category) {
        val intent = Intent(this.activity, FragmentContainerActivity::class.java)
        val b = Bundle()
        b.putString("name", category.name)
        b.putString("key", category.key)
        b.putString("uri", category.uri)
        intent.putExtras(b)
        startActivity(intent)
    }
}