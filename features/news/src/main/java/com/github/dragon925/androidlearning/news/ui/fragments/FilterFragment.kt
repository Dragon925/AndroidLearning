package com.github.dragon925.androidlearning.news.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.github.dragon925.androidlearning.core.api.ui.ComponentViewModel
import com.github.dragon925.androidlearning.core.api.ui.MultiViewModelFactory
import com.github.dragon925.androidlearning.core.api.ui.UIState
import com.github.dragon925.androidlearning.core.api.ui.create
import com.github.dragon925.androidlearning.core.api.ui.createFactoryByViewModel
import com.github.dragon925.androidlearning.news.R
import com.github.dragon925.androidlearning.news.databinding.FragmentFilterBinding
import com.github.dragon925.androidlearning.news.di.NewsDeps
import com.github.dragon925.androidlearning.news.di.components.DaggerNewsListComponent
import com.github.dragon925.androidlearning.news.di.components.FilterComponent
import com.github.dragon925.androidlearning.news.di.components.NewsListComponent
import com.github.dragon925.androidlearning.news.ui.adapters.FilterListAdapter
import com.github.dragon925.androidlearning.news.ui.models.FilterUIState
import com.github.dragon925.androidlearning.news.ui.viewmodels.FilterViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

class FilterFragment : Fragment() {
    companion object {
        const val CHOSEN_FILTERS = "chosenFilters"
        const val REQUEST_KEY = "FilterFragment-Request"
        const val RESULT_CODE = "FilterFragment-ResultCode"
        const val RESULT_KEY = "FilterFragment-Result"

        const val RESULT_OK = 0
        const val RESULT_CANCEL = -1
    }

    private var chosenFilters: Array<String> = emptyArray()

    private var _binding: FragmentFilterBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: MultiViewModelFactory

    private val newsComponentViewModel: ComponentViewModel<NewsListComponent> by activityViewModels {
        ComponentViewModel.createBy<NewsListComponent, NewsDeps> {
            DaggerNewsListComponent.builder().deps(this).build()
        }
    }
    private val filterComponentViewModel: ComponentViewModel<FilterComponent> by viewModels {
        ComponentViewModel.createBy(newsComponentViewModel.component) {
            filterComponent().build()
        }
    }
    private val filterViewModel: FilterViewModel by viewModels {
        createFactoryByViewModel { viewModelFactory.create<FilterViewModel>() }
    }
    private val filterListAdapter = FilterListAdapter(::updateFilter)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            chosenFilters = it.getStringArray(CHOSEN_FILTERS) ?: emptyArray()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        filterComponentViewModel.component.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentFilterBinding.inflate(inflater, container, false)
        .also { _binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvFilters.adapter = filterListAdapter

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                filterViewModel.state.collect(::updateState)
            }
        }

        filterViewModel.checkCategory(*chosenFilters, isChecked = true)

        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId) {
                R.id.action_apply_filter -> {
                    val chosenFilters = filterViewModel.currentChosenCategories.toTypedArray()
                    setFragmentResult(
                        REQUEST_KEY,
                        bundleOf(
                            RESULT_CODE to RESULT_OK,
                            RESULT_KEY to chosenFilters
                        )
                    )
                    findNavController().navigateUp()
                    true
                }
                else -> false
            }
        }
        binding.toolbar.setNavigationOnClickListener {
            setFragmentResult(REQUEST_KEY, bundleOf(RESULT_CODE to RESULT_CANCEL))
            findNavController().navigateUp()
        }
    }

    private fun updateState(state: UIState<FilterUIState, String>) {
        with(binding) {
            piLoading.isVisible = state.isLoading
            rvFilters.isGone = state.isLoading
            toolbar.menu.findItem(R.id.action_apply_filter).isEnabled = !state.isLoading
        }

        state.data?.filters?.let { filterListAdapter.submitList(it) }
    }

    private fun updateFilter(id: String, isChecked: Boolean) {
        filterViewModel.checkCategory(id, isChecked = isChecked)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}