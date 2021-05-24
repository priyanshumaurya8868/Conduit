package com.priyanshumaurya8868.realworld.io.conduit.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.priyanshumaurya8868.realworld.io.conduit.AuthViewModel
import com.priyanshumaurya8868.realworld.io.conduit.databinding.FragmentLoginSignupBinding

class LoginFragment : Fragment() {
    private val authViewModel: AuthViewModel by activityViewModels()
    private var _binding: FragmentLoginSignupBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginSignupBinding.inflate(inflater, container, false)
        _binding?.usernameEtLayout?.visibility = View.GONE
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding?.apply {
            SubmitBtn.setOnClickListener {
                authViewModel.login(loginEmailEt.text.toString(), loginPswdEt.text.toString())
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}