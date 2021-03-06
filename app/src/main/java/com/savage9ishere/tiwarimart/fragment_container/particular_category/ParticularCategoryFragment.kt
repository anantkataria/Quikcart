package com.savage9ishere.tiwarimart.fragment_container.particular_category

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.savage9ishere.tiwarimart.R
import com.savage9ishere.tiwarimart.databinding.ParticularCategoryFragmentBinding
import com.savage9ishere.tiwarimart.main_flow.ui.home.Item

class ParticularCategoryFragment : Fragment() {
    private lateinit var viewModel: ParticularCategoryViewModel
    private var categoryName: String? = null
    private var categoryImageUri: String? = null
    private var categoryKey: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        val binding = ParticularCategoryFragmentBinding.inflate(inflater)

        val bundle = requireActivity().intent.extras
        categoryName = bundle!!.getString("name")
        categoryImageUri  = bundle.getString("uri")
        categoryKey = bundle.getString("key")

        (activity as AppCompatActivity).supportActionBar?.title = categoryName

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
        b.putString("category_name", categoryName)
        findNavController().navigate(R.id.action_particularCategoryFragment_to_particularItemFragment, b)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragment_container_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.cart ->{
                findNavController().navigate(R.id.action_particularCategoryFragment_to_cartFragment)
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }
}