package com.github.dragon925.androidlearning

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.github.dragon925.androidlearning.databinding.ActivityMainBinding
import com.github.dragon925.androidlearning.help.ui.fragments.HelpCategoriesFragment
import com.github.dragon925.androidlearning.news.ui.fragments.NewsFragment
import com.github.dragon925.androidlearning.news.ui.viewmodels.UnreadNewsViewModel
import com.github.dragon925.androidlearning.profile.ui.fragments.ProfileFragment
import com.github.dragon925.androidlearning.search.ui.fragments.SearchFragment
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val unreadNewsViewModel: UnreadNewsViewModel by viewModels()
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
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

        unreadNewsViewModel.unreadCount.observeOn(AndroidSchedulers.mainThread())
            .subscribe { unreadCount ->
                binding.bottomNavBar.getOrCreateBadge(R.id.screen_news).apply {
                    isVisible = unreadCount > 0
                    number = unreadCount
                }
            }
            .also(compositeDisposable::add)
    }

    private fun initNavigation() {
        with(binding) {
            bottomNavBar.selectedItemId = R.id.screen_help
            bottomNavBar.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.screen_help -> {
                        supportFragmentManager.beginTransaction()
                            .replace(mainNavContainer.id, HelpCategoriesFragment.newInstance())
                            .commit()
                        true
                    }

                    R.id.screen_profile -> {
                        supportFragmentManager.beginTransaction()
                            .replace(mainNavContainer.id, ProfileFragment.newInstance(0))
                            .commit()
                        true
                    }

                    R.id.screen_search -> {
                        supportFragmentManager.beginTransaction()
                            .replace(mainNavContainer.id, SearchFragment.newInstance())
                            .commit()
                        true
                    }

                    R.id.screen_news -> {
                        supportFragmentManager.beginTransaction()
                            .replace(mainNavContainer.id, NewsFragment.newInstance())
                            .commit()
                        true
                    }

                    else -> false
                }
            }
            btnHelp.setOnClickListener {
                bottomNavBar.selectedItemId = R.id.screen_help
                supportFragmentManager.beginTransaction()
                    .replace(mainNavContainer.id, HelpCategoriesFragment.newInstance())
                    .commit()
            }
        }
    }
}
