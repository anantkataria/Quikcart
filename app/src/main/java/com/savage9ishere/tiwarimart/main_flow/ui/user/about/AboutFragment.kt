package com.savage9ishere.tiwarimart.main_flow.ui.user.about

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.savage9ishere.tiwarimart.R
import com.savage9ishere.tiwarimart.databinding.AboutFragmentBinding

class AboutFragment : Fragment() {

    private lateinit var viewModel: AboutViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = AboutFragmentBinding.inflate(inflater)

        viewModel = ViewModelProvider(this).get(AboutViewModel::class.java)

        val adapter = AboutAdapter()
        binding.teamMembersRecyclerView.adapter = adapter
        binding.teamMembersRecyclerView.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        binding.lifecycleOwner = this

        viewModel.items.observe(viewLifecycleOwner, {
            it?.let {
                adapter.submitList(it.toList())
            }
        })

        return binding.root
    }
}