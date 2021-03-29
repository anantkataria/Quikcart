package com.savage9ishere.tiwarimart.fragment_container.particular_item.review

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.savage9ishere.tiwarimart.R
import com.savage9ishere.tiwarimart.databinding.ReviewFragmentBinding
import com.savage9ishere.tiwarimart.main_flow.ui.home.Item
import javax.security.auth.login.LoginException

class ReviewFragment : Fragment() {

    companion object {
        fun newInstance() = ReviewFragment()
    }

    private lateinit var viewModel: ReviewViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = ReviewFragmentBinding.inflate(inflater)

        viewModel = ViewModelProvider(this).get(ReviewViewModel::class.java)

        val item : Item? = requireArguments().getParcelable("item")
        val categoryName : String? = requireArguments().getString("category_name")

        Log.e("123321", "category name = ${categoryName}, item name = ${item!!.name}")

        return binding.root
    }

}