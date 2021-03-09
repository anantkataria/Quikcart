package com.savage9ishere.tiwarimart.settings.chooser

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.savage9ishere.tiwarimart.R

class ChooserFragment : Fragment() {

    companion object {
        fun newInstance() = ChooserFragment()
    }

    private lateinit var viewModel: ChooserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.chooser_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ChooserViewModel::class.java)
        // TODO: Use the ViewModel
    }

}