package com.github.dragon925.androidlearning

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.github.dragon925.androidlearning.databinding.ActivityMainBinding
import com.github.dragon925.androidlearning.help.ui.fragments.HelpCategoriesFragment
import com.github.dragon925.androidlearning.news.ui.fragments.NewsFragment
import com.github.dragon925.androidlearning.profile.ui.fragments.ProfileFragment
import com.github.dragon925.androidlearning.search.ui.fragments.SearchFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

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

        binding.bottomNavBar.selectedItemId = R.id.screen_help
        binding.bottomNavBar.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.screen_help -> {
                    supportFragmentManager.beginTransaction()
                        .replace(binding.mainNavContainer.id, HelpCategoriesFragment.newInstance())
                        .commit()
                    true
                }
                R.id.screen_profile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(binding.mainNavContainer.id, ProfileFragment.newInstance(0))
                        .commit()
                    true
                }
                R.id.screen_search -> {
                    supportFragmentManager.beginTransaction()
                        .replace(binding.mainNavContainer.id, SearchFragment.newInstance())
                        .commit()
                    true
                }
                R.id.screen_news -> {
                    supportFragmentManager.beginTransaction()
                        .replace(binding.mainNavContainer.id, NewsFragment.newInstance())
                        .commit()
                    true
                }
                else -> false
            }
        }
        binding.btnHelp.setOnClickListener {
            binding.bottomNavBar.selectedItemId = R.id.screen_help
            supportFragmentManager.beginTransaction()
                .replace(binding.mainNavContainer.id, HelpCategoriesFragment.newInstance())
                .commit()
        }
    }
}
