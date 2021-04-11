package com.savage9ishere.tiwarimart.main_flow.ui.user.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.savage9ishere.tiwarimart.databinding.ProfileFragmentBinding

class ProfileFragment : Fragment() {

    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = ProfileFragmentBinding.inflate(inflater)

        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)



        return binding.root
    }
}