package com.savage9ishere.tiwarimart.main_flow.ui.user.games

import android.content.pm.ActivityInfo
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.savage9ishere.tiwarimart.R
import com.savage9ishere.tiwarimart.databinding.GamesFragmentBinding

class GamesFragment : Fragment() {

    private lateinit var viewModel: GamesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = GamesFragmentBinding.inflate(inflater)

        viewModel = ViewModelProvider(this).get(GamesViewModel::class.java)

        binding.slotMachineGameImageButton.setOnClickListener {
            if (findNavController().currentDestination?.id == R.id.gamesFragment){
                findNavController().navigate(R.id.action_gamesFragment_to_slotMachineFragment)
            }
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }
}