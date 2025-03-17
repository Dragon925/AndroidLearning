package com.github.dragon925.androidlearning.news.ui.activities

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.DEFAULT_ARGS_KEY
import androidx.lifecycle.viewmodel.MutableCreationExtras
import com.github.dragon925.androidlearning.R
import com.github.dragon925.androidlearning.common.ui.UIState
import com.github.dragon925.androidlearning.common.ui.getAssetDrawable
import com.github.dragon925.androidlearning.databinding.ActivityNewsDetailsBinding
import com.github.dragon925.androidlearning.news.ui.models.NewsDetailItem
import com.github.dragon925.androidlearning.news.ui.viewmodels.NewsDetailsViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable

class NewsDetailsActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_NEWS_ID = "NewsDetailsID"
        const val EXTRA_NEWS_TITLE = "NewsDetailsTitle"
        private const val DEFAULT_NEWS_ID = -1
    }

    private var newsId: Int = DEFAULT_NEWS_ID
    private var newsTitle: String = ""

    private lateinit var binding: ActivityNewsDetailsBinding

    private val viewModel: NewsDetailsViewModel by viewModels(
        extrasProducer = {
            MutableCreationExtras(defaultViewModelCreationExtras).apply {
                this[DEFAULT_ARGS_KEY] = bundleOf(
                    NewsDetailsViewModel.NEWS_DETAILS_ID to newsId,
                )
            }
        },
        factoryProducer = { NewsDetailsViewModel.Factory }
    )
    private val compositeDisposable = CompositeDisposable()

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

        viewModel.state.observeOn(AndroidSchedulers.mainThread())
            .subscribe(::updateState)
            .also(compositeDisposable::add)
    }

    private fun initViews(details: NewsDetailItem) {
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

    private fun updateState(state: UIState<NewsDetailItem, String>) {
        with(binding) {
            piLoading.isVisible = state.isLoading
            nsvContent.isGone = state.isLoading
        }

        state.data?.let { initViews(it) }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}