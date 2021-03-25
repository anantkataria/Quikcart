package com.savage9ishere.tiwarimart.fragment_container.particular_category

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.savage9ishere.tiwarimart.R
import com.savage9ishere.tiwarimart.databinding.ParticularCategoryFragmentBinding
import com.savage9ishere.tiwarimart.main_flow.ui.home.Item

class ParticularCategoryFragment : Fragment() {
    private lateinit var viewModel: ParticularCategoryViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        val binding = ParticularCategoryFragmentBinding.inflate(inflater)

        val bundle = requireActivity().intent.extras
        val categoryName : String? = bundle!!.getString("name")
        val categoryImageUri : String? = bundle.getString("uri")
        val categoryKey : String? = bundle.getString("key")

        val viewModelFactory = ParticularCategoryViewModelFactory(categoryName!!)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ParticularCategoryViewModel::class.java)

        val adapter = ItemsAdapter{itemOnClick(it)}
        binding.recyclerViewItems.adapter = adapter
        binding.recyclerViewItems.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.lifecycleOwner = this

        viewModel.items.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it.toList())
            }
        })

        return binding.root
    }

    private fun itemOnClick(item: Item) {
        val b = Bundle()
        b.putParcelable("item", item)
        findNavController().navigate(R.id.action_particularCategoryFragment_to_particularItemFragment, b)
    }

}