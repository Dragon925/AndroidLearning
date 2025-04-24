package com.github.dragon925.androidlearning.authorization.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.github.dragon925.androidlearning.authorization.databinding.FragmentAuthorizationBinding
import com.github.dragon925.androidlearning.authorization.domain.models.AuthState
import com.github.dragon925.androidlearning.authorization.ui.textChanges
import com.github.dragon925.androidlearning.authorization.ui.viewmodels.AuthViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch


class AuthorizationFragment : Fragment() {

    companion object {

        private fun validateLoginAndPassword(login: CharSequence, password: CharSequence): Boolean {
            return login.trim().length >= 6 && password.trim().length >= 6
        }
    }

    private val authViewModel: AuthViewModel by activityViewModels()

    private var _binding: FragmentAuthorizationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentAuthorizationBinding.inflate(inflater, container, false)
        .also { _binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                combine(
                    binding.etEmail.textChanges(),
                    binding.etPassword.textChanges(),
                    ::validateLoginAndPassword
                ).collect {
                    binding.btnEnter.isEnabled = it
                }
            }
        }

        binding.btnEnter.setOnClickListener {
            authViewModel.setState(AuthState.AUTHORIZED)
        }

        binding.toolbar.setNavigationOnClickListener {
            authViewModel.setState(AuthState.CANCELED)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}