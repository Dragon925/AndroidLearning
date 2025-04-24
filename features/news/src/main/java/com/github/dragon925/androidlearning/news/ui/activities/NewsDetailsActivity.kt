package com.github.dragon925.androidlearning.news.ui.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil3.load
import com.github.dragon925.androidlearning.core.api.ui.ComponentViewModel
import com.github.dragon925.androidlearning.core.api.ui.UIState
import com.github.dragon925.androidlearning.core.api.ui.createFactoryByViewModel
import com.github.dragon925.androidlearning.news.R
import com.github.dragon925.androidlearning.news.databinding.ActivityNewsDetailsBinding
import com.github.dragon925.androidlearning.news.di.NewsDeps
import com.github.dragon925.androidlearning.news.di.components.DaggerNewsDetailsComponent
import com.github.dragon925.androidlearning.news.di.components.NewsDetailsComponent
import com.github.dragon925.androidlearning.news.ui.models.NewsDetailItem
import com.github.dragon925.androidlearning.news.ui.viewmodels.NewsDetailsViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import jakarta.inject.Inject
import kotlinx.coroutines.launch

class NewsDetailsActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_NEWS_ID = "NewsDetailsID"
        const val EXTRA_NEWS_TITLE = "NewsDetailsTitle"
        private const val DEFAULT_NEWS_ID = ""
    }

    private var newsId: String = DEFAULT_NEWS_ID
    private var newsTitle: String = ""

    private lateinit var binding: ActivityNewsDetailsBinding

    @Inject
    internal lateinit var viewModelFactory: NewsDetailsViewModel.Factory

    private val detailsComponentViewModel: ComponentViewModel<NewsDetailsComponent> by viewModels {
        ComponentViewModel.createBy<NewsDetailsComponent, NewsDeps> {
            DaggerNewsDetailsComponent.builder()
                .newsId(newsId)
                .deps(this)
                .build()
        }
    }
    private val viewModel: NewsDetailsViewModel by viewModels {
        createFactoryByViewModel {
            viewModelFactory.create(newsId)
        }
    }
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

        newsId = intent.extras?.getString(EXTRA_NEWS_ID) ?: DEFAULT_NEWS_ID
        newsTitle = intent.extras?.getString(EXTRA_NEWS_TITLE) ?: ""

        binding.toolbar.setNavigationOnClickListener { finish() }
        binding.toolbar.title = newsTitle

        detailsComponentViewModel.component.inject(this)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect(::updateState)
            }
        }
    }

    private fun initViews(details: NewsDetailItem) {
        with(binding) {
            tvTitle.text = details.name
            tvDate.text = details.date
            tvOrganization.text = details.organizer
            tvLocation.text = details.address
            tvPhones.text = details.phoneNumbers.joinToString("\n")
            for ((i, image) in listOf(ivImage1, ivImage2, ivImage3).withIndex()) {
                val photo = details.photos.getOrNull(i)
                image.isGone = photo.isNullOrBlank()
                photo?.let { path ->
                    image.load(path)
                }
            }
            tvDescription.text = details.description

            val firstFiveAvatars = listOf(ivAvatar1, ivAvatar2, ivAvatar3, ivAvatar4, ivAvatar5)
            details.members.take(5).forEachIndexed { index, member ->
                firstFiveAvatars[index].isVisible = true
                firstFiveAvatars[index].load(member.avatar)
            }
            val otherMembers = details.members.drop(5).size
            if (otherMembers > 0) {
                tvMoreAvatars.isVisible = true
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