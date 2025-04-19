package com.github.dragon925.androidlearning.authorization.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.github.dragon925.androidlearning.authorization.domain.models.AuthState
import com.github.dragon925.androidlearning.authorization.ui.viewmodels.AuthViewModel
import com.github.dragon925.androidlearning.databinding.FragmentAuthorizationBinding
import com.jakewharton.rxbinding4.widget.textChanges
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable


class AuthorizationFragment : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance() = AuthorizationFragment()

        private fun validateLoginAndPassword(login: CharSequence, password: CharSequence): Boolean {
            return login.trim().length >= 6 && password.trim().length >= 6
        }
    }

    private lateinit var loginPasswordChecker: Disposable

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
        loginPasswordChecker = Observable.combineLatest(
            binding.etEmail.textChanges(),
            binding.etPassword.textChanges(),
            Companion::validateLoginAndPassword
        ).subscribe { binding.btnEnter.isEnabled = it }

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