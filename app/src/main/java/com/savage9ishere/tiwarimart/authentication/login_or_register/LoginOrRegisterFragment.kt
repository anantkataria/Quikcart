package com.savage9ishere.tiwarimart.authentication.login_or_register

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.savage9ishere.tiwarimart.R

class LoginOrRegisterFragment : Fragment() {

    companion object {
        fun newInstance() = LoginOrRegisterFragment()
    }

    private lateinit var viewModel: LoginOrRegisterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.login_or_register_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LoginOrRegisterViewModel::class.java)
        // TODO: Use the ViewModel
    }

}