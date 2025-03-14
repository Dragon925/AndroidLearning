package com.github.dragon925.androidlearning.authorization.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.github.dragon925.androidlearning.MainActivity
import com.github.dragon925.androidlearning.R
import com.github.dragon925.androidlearning.databinding.ActivityAuthorizationBinding
import com.jakewharton.rxbinding4.widget.textChanges
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable

class AuthorizationActivity : AppCompatActivity() {

    companion object {
        private fun validateLoginAndPassword(login: CharSequence, password: CharSequence): Boolean {
            return login.trim().length >= 6 && password.trim().length >= 6
        }
    }

    private lateinit var binding: ActivityAuthorizationBinding

    private lateinit var loginPasswordChecker: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAuthorizationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()
    }

    private fun initViews() {
        loginPasswordChecker = Observable.combineLatest(
            binding.etEmail.textChanges(),
            binding.etPassword.textChanges(),
            ::validateLoginAndPassword
        ).subscribe { binding.btnEnter.isEnabled = it }

        binding.btnEnter.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        binding.toolbar.setNavigationOnClickListener { finish() }
    }

    override fun onDestroy() {
        super.onDestroy()
        loginPasswordChecker.dispose()
    }
}