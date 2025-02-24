package com.github.dragon925.androidlearning.news.ui.activities

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.github.dragon925.androidlearning.R
import com.github.dragon925.androidlearning.common.ui.getAssetDrawable
import com.github.dragon925.androidlearning.databinding.ActivityNewsDetailsBinding
import com.github.dragon925.androidlearning.news.data.models.Event
import com.github.dragon925.androidlearning.news.data.repositories.EventRepository
import com.github.dragon925.androidlearning.news.ui.utils.getDateString

class NewsDetailsActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_NEWS_ID = "NewsDetailsID"
        const val EXTRA_NEWS_TITLE = "NewsDetailsTitle"
        private const val DEFAULT_NEWS_ID = -1
    }

    private var newsId: Int = DEFAULT_NEWS_ID
    private lateinit var newsTitle: String

    private lateinit var binding: ActivityNewsDetailsBinding

    private lateinit var event: Event

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityNewsDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        newsId = intent.extras?.getInt(EXTRA_NEWS_ID) ?: DEFAULT_NEWS_ID
        newsTitle = intent.extras?.getString(EXTRA_NEWS_TITLE) ?: ""

        event = EventRepository.getEvents(assets).find { it.id == newsId }!!

        initViews()
    }

    private fun initViews() {
        with(binding) {
            toolbar.setNavigationOnClickListener { finish() }
            toolbar.title = newsTitle

            tvTitle.text = event.name
            tvDate.text = getDateString(
                this@NewsDetailsActivity, event.startDate, event.endDate
            )
            tvOrganization.text = event.organizer
            tvLocation.text = event.address
            tvPhones.text = event.phoneNumbers.joinToString("\n")
            for ((i, image) in listOf(ivImage1, ivImage2, ivImage3).withIndex()) {
                event.photos.getOrNull(i)?.let { path ->
                    image.setImageDrawable(
                        this@NewsDetailsActivity.getAssetDrawable(path)
                    )
                }
            }
            tvDescription.text = event.description

            val firstFiveAvatars = listOf(ivAvatar1, ivAvatar2, ivAvatar3, ivAvatar4, ivAvatar5)
            event.members.take(5).forEachIndexed { index, member ->
                firstFiveAvatars[index].visibility = View.VISIBLE
                firstFiveAvatars[index].setImageDrawable(
                    this@NewsDetailsActivity.getAssetDrawable(member.avatar)
                )
            }
            val otherMembers = event.members.drop(5).size
            if (otherMembers > 0) {
                tvMoreAvatars.visibility = View.VISIBLE
                tvMoreAvatars.text = resources.getString(R.string.more_count, otherMembers)
            }
        }
    }
}