package com.savage9ishere.tiwarimart.main_flow.ui.user.customer_support

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.savage9ishere.tiwarimart.R

class CustomerSupportFragment : Fragment() {

    private lateinit var viewModel: CustomerSupportViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.customer_support_fragment, container, false)
    }

}