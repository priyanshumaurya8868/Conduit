package com.priyanshumaurya8868.realworld.io.conduit.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.priyanshumaurya8868.realworld.io.conduit.AuthViewModel
import com.priyanshumaurya8868.realworld.io.conduit.databinding.FragmentSettingsBinding


class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val authViewModel by activityViewModels<AuthViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        retainInstance = true
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding?.apply {
            authViewModel.user.observe({ lifecycle }) {
                updateUrlPfpEt.setText(it?.image ?: "")
                updateUsernameEt.setText(it?.username ?: "")
                updateBioEt.setText(it?.bio ?: "")
                updateEmailEt.setText(it?.email ?: "")
                updatePasswordEt.setText("")
            }
            updateSubmitBtn.setOnClickListener {
                authViewModel.updateUser(
                    image = updateUrlPfpEt.text.toString(),
                    username = updateUsernameEt.text.toString()
                        .takeIf { it.isNotBlank() }, // blank  is an empty string with whiteSpaces (i.e " /t")
                    bio = updateBioEt.text.toString(),
                    email = updateEmailEt.text.toString().takeIf { it.isNotBlank() },
                    password = updatePasswordEt.text.toString().takeIf { it.isNotBlank() }
                )
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}