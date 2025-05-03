package com.github.dragon925.androidlearning.news.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.github.dragon925.androidlearning.core.api.ui.ComponentViewModel
import com.github.dragon925.androidlearning.core.api.ui.MultiViewModelFactory
import com.github.dragon925.androidlearning.core.api.ui.create
import com.github.dragon925.androidlearning.core.api.ui.createFactoryByViewModel
import com.github.dragon925.androidlearning.core.api.ui.theme.AppTheme
import com.github.dragon925.androidlearning.news.di.NewsDeps
import com.github.dragon925.androidlearning.news.di.components.DaggerNewsListComponent
import com.github.dragon925.androidlearning.news.di.components.NewsListComponent
import com.github.dragon925.androidlearning.news.ui.viewmodels.NewsViewModel
import com.github.dragon925.androidlearning.news.ui.viewmodels.UnreadNewsViewModel
import jakarta.inject.Inject

class NewsFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: MultiViewModelFactory

    private val newsComponentViewModel: ComponentViewModel<NewsListComponent> by activityViewModels {
        ComponentViewModel.createBy<NewsListComponent, NewsDeps> {
            DaggerNewsListComponent.builder().deps(this).build()
        }
    }
    private val unreadNewsViewModel: UnreadNewsViewModel by activityViewModels()
    private val newsViewModel: NewsViewModel by viewModels {
        createFactoryByViewModel { viewModelFactory.create<NewsViewModel>() }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        newsComponentViewModel.component.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        setContent {
            AppTheme {
                NewsListScreen(newsViewModel, unreadNewsViewModel, findNavController())
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFragmentResultListener(FilterFragment.REQUEST_KEY) { _, bundle ->
            if (bundle.getInt(FilterFragment.RESULT_CODE) == FilterFragment.RESULT_OK) {
                val result = bundle.getStringArray(FilterFragment.RESULT_KEY)?.toList() ?: emptyList()
                newsViewModel.setFilters(result)
            }
        }
    }
}