package com.savage9ishere.tiwarimart.fragment_container.particular_item

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.savage9ishere.tiwarimart.R

class ParticularItemFragment : Fragment() {

    companion object {
        fun newInstance() = ParticularItemFragment()
    }

    private lateinit var viewModel: ParticularItemViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.particular_item_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ParticularItemViewModel::class.java)
        // TODO: Use the ViewModel
    }

}