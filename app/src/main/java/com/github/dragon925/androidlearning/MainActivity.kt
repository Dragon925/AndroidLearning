package com.github.dragon925.androidlearning

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.github.dragon925.androidlearning.authorization.ui.viewmodels.AuthViewModel
import com.github.dragon925.androidlearning.databinding.ActivityMainBinding
import com.github.dragon925.androidlearning.news.ui.viewmodels.UnreadNewsViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

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

        val navController = initNavigation()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                authViewModel.success.collect { isSuccess ->
                    if (isSuccess) {
                        navController.navigate(R.id.action_screen_authorization_to_screen_help)
                        binding.bottomNavBar.isVisible = true
                        binding.btnHelp.isVisible = true
                    } else {
                        finish()
                    }
                }
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

    private fun initNavigation(): NavController {
        with(binding) {
            val navHostFragment =
                supportFragmentManager.findFragmentById(mainNavContainer.id) as NavHostFragment
            val navController = navHostFragment.navController

            bottomNavBar.setupWithNavController(navController)

            btnHelp.setOnClickListener {
                bottomNavBar.selectedItemId = R.id.screen_help
                navController.navigate(R.id.screen_help)
            }

            return navController
        }
    }
}
