package com.github.dragon925.androidlearning

import android.os.Bundle
import android.view.Menu
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.github.dragon925.androidlearning.databinding.ActivityMainBinding
import com.github.dragon925.androidlearning.ui.adapters.FriendsListAdapter
import com.github.dragon925.androidlearning.ui.adapters.SimpleItemDecoration
import com.github.dragon925.androidlearning.ui.models.FriendItem

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val friendsAdapter = FriendsListAdapter()

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

        with(binding) {
            setSupportActionBar(toolbar)

            binding.bottomNavBar.selectedItemId = R.id.screen_profile

            rvFriends.addItemDecoration(SimpleItemDecoration(this@MainActivity))
            rvFriends.adapter = friendsAdapter
        }

        initSampleData()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_profile, menu)
        return true
    }

    private fun initSampleData() {
        with(binding) {
            ivAvatar.setImageResource(R.drawable.image_man)
            tvName.text = "Константинов Денис"
            tvBirthday.text = "01 февраля 1980"
            tvFieldOfActivity.text = "Хирургия, травматология"
            friendsAdapter.submitList(
                listOf(
                    FriendItem(1, R.drawable.avatar_1, "Дмитрий Валерьевич"),
                    FriendItem(2, R.drawable.avatar_2, "Евгений Александров"),
                    FriendItem(3, R.drawable.avatar_3, "Виктор Кузнецов"),
                ),
            )
        }
    }
}
