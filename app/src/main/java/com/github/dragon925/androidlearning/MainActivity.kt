package com.github.dragon925.androidlearning

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isGone
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.github.dragon925.androidlearning.authorization.domain.models.AuthState
import com.github.dragon925.androidlearning.authorization.ui.viewmodels.AuthViewModel
import com.github.dragon925.androidlearning.databinding.ActivityMainBinding
import com.github.dragon925.androidlearning.help.R as HelpR
import com.github.dragon925.androidlearning.authorization.R as AuthR
import com.github.dragon925.androidlearning.news.ui.viewmodels.UnreadNewsViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    private val unreadNewsViewModel: UnreadNewsViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        initNavigation()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                authViewModel.state.collect(::updateAuth)
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                authViewModel.isAuthorized.collect(::updateUI)
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                unreadNewsViewModel.unreadCount.collect { unreadCount ->
                    binding.bottomNavBar.getOrCreateBadge(R.id.screen_news).apply {
                        isVisible = unreadCount > 0
                        number = unreadCount
                    }
                }
            }
        }
    }

    private fun initNavigation() {
        with(binding) {
            val navHostFragment =
                supportFragmentManager.findFragmentById(mainNavContainer.id) as NavHostFragment
            navController = navHostFragment.navController

            bottomNavBar.setupWithNavController(navController)

            btnHelp.setOnClickListener {
                bottomNavBar.selectedItemId = R.id.screen_help
            }
        }
    }

    private fun updateAuth(state: AuthState) {
        when (state) {
            AuthState.AUTHORIZED -> {
                navController.navigate(
                    HelpR.id.screen_help,
                    null,
                    NavOptions.Builder()
                        .setPopUpTo(HelpR.id.screen_help, false)
                        .build()
                )
            }
            AuthState.CANCELED -> finish()
            AuthState.UNAUTHORIZED -> {
                navController.navigate(
                    AuthR.id.screen_auth,
                    null,
                    NavOptions.Builder()
                        .setPopUpTo(AuthR.id.screen_auth, true)
                        .build()
                )
            }
        }
    }

    private fun updateUI(isAuthorized: Boolean) {
        binding.btnHelp.isGone = !isAuthorized
        binding.bottomNavBar.isGone = !isAuthorized
    }

}
