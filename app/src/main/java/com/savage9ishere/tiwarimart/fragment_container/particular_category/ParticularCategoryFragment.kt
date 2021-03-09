package com.savage9ishere.tiwarimart.fragment_container.particular_category

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.savage9ishere.tiwarimart.R

class ParticularCategoryFragment : Fragment() {

    companion object {
        fun newInstance() = ParticularCategoryFragment()
    }

    private lateinit var viewModel: ParticularCategoryViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.particular_category_fragment, container, false)

//        activity?.supportFragmentManager?.beginTransaction().add()

        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ParticularCategoryViewModel::class.java)
        // TODO: Use the ViewModel
    }

}