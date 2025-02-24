package com.github.dragon925.androidlearning.news.ui.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.github.dragon925.androidlearning.R
import com.github.dragon925.androidlearning.common.data.service.DataLoadingServiceHelper
import com.github.dragon925.androidlearning.common.ui.getAssetDrawable
import com.github.dragon925.androidlearning.common.ui.readParcelable
import com.github.dragon925.androidlearning.databinding.ActivityNewsDetailsBinding
import com.github.dragon925.androidlearning.news.ui.models.NewsDetailItem
import com.github.dragon925.androidlearning.news.ui.utils.toNewsDetailItem

class NewsDetailsActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_NEWS_ID = "NewsDetailsID"
        const val EXTRA_NEWS_TITLE = "NewsDetailsTitle"
        const val SAVED_DETAILS = "NewsDetailsActivity-savedNewsDetails"
        private const val DEFAULT_NEWS_ID = -1
    }

    private var newsId: Int = DEFAULT_NEWS_ID
    private var newsTitle: String = ""

    private lateinit var binding: ActivityNewsDetailsBinding

    private lateinit var details: NewsDetailItem

    private val serviceHelper = DataLoadingServiceHelper(
        { events, _ ->
            handleLoadedData(events.find { it.id == newsId }!!.toNewsDetailItem(this))
        },
        ::handleErrorLoadData
    )

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

        binding.toolbar.setNavigationOnClickListener { finish() }
        binding.toolbar.title = newsTitle

        if (savedInstanceState == null || savedInstanceState.isEmpty) {
            updateUI(true)
            serviceHelper.bindService(this)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (::details.isInitialized) {
            outState.putParcelable(SAVED_DETAILS, details)
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        if (!savedInstanceState.containsKey(SAVED_DETAILS)) return

        savedInstanceState.readParcelable<NewsDetailItem>(SAVED_DETAILS)?.let {
            handleLoadedData(it)
        }
    }

    private fun handleErrorLoadData() {
        Log.e("NewsDetailsActivity", "loadDataWithService-onError")
    }

    private fun handleLoadedData(newsDetails: NewsDetailItem) {
        runOnUiThread {
            details = newsDetails
            initViews()
            updateUI(false)
        }
    }

    private fun initViews() {
        with(binding) {
            tvTitle.text = details.name
            tvDate.text = details.date
            tvOrganization.text = details.organizer
            tvLocation.text = details.address
            tvPhones.text = details.phoneNumbers.joinToString("\n")
            for ((i, image) in listOf(ivImage1, ivImage2, ivImage3).withIndex()) {
                details.photos.getOrNull(i)?.let { path ->
                    image.setImageDrawable(
                        this@NewsDetailsActivity.getAssetDrawable(path)
                    )
                }
            }
            tvDescription.text = details.description

            val firstFiveAvatars = listOf(ivAvatar1, ivAvatar2, ivAvatar3, ivAvatar4, ivAvatar5)
            details.members.take(5).forEachIndexed { index, member ->
                firstFiveAvatars[index].visibility = View.VISIBLE
                firstFiveAvatars[index].setImageDrawable(
                    this@NewsDetailsActivity.getAssetDrawable(member.avatar)
                )
            }
            val otherMembers = details.members.drop(5).size
            if (otherMembers > 0) {
                tvMoreAvatars.visibility = View.VISIBLE
                tvMoreAvatars.text = resources.getString(R.string.more_count, otherMembers)
            }
        }
    }

    private fun updateUI(showLoading: Boolean) {
        with(binding) {
            piLoading.visibility = if (showLoading) View.VISIBLE else View.GONE
            nsvContent.visibility = if (showLoading) View.GONE else View.VISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceHelper.unbindService(this)
    }
}