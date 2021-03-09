package com.savage9ishere.tiwarimart.authentication.store_closed

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.savage9ishere.tiwarimart.R

class StoreClosedFragment : Fragment() {

    companion object {
        fun newInstance() = StoreClosedFragment()
    }

    private lateinit var viewModel: StoreClosedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.store_closed_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(StoreClosedViewModel::class.java)
        // TODO: Use the ViewModel
    }

}