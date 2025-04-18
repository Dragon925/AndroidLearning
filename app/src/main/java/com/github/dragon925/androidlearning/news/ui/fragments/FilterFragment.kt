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
import androidx.navigation.fragment.findNavController
import com.github.dragon925.androidlearning.R
import com.github.dragon925.androidlearning.common.ui.ComponentViewModel
import com.github.dragon925.androidlearning.common.ui.MultiViewModelFactory
import com.github.dragon925.androidlearning.common.ui.UIState
import com.github.dragon925.androidlearning.common.ui.create
import com.github.dragon925.androidlearning.common.ui.createFactoryByViewModel
import com.github.dragon925.androidlearning.databinding.FragmentFilterBinding
import com.github.dragon925.androidlearning.news.di.components.FilterComponent
import com.github.dragon925.androidlearning.news.di.components.NewsListComponent
import com.github.dragon925.androidlearning.news.ui.adapters.FilterListAdapter
import com.github.dragon925.androidlearning.news.ui.models.FilterUIState
import com.github.dragon925.androidlearning.news.ui.viewmodels.FilterViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import jakarta.inject.Inject

class FilterFragment : Fragment() {
    companion object {
        const val CHOSEN_FILTERS = "chosenFilters"
        const val REQUEST_KEY = "FilterFragment-Request"
        const val RESULT_CODE = "FilterFragment-ResultCode"
        const val RESULT_KEY = "FilterFragment-Result"

        const val RESULT_OK = 0
        const val RESULT_CANCEL = -1

        @JvmStatic
        fun newInstance(chosenFilters: Array<String> = emptyArray()) =
            FilterFragment().apply {
                arguments = Bundle().apply {
                    putStringArray(CHOSEN_FILTERS, chosenFilters)
                }
            }
    }

    private var chosenFilters: Array<String> = emptyArray()

    private var _binding: FragmentFilterBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: MultiViewModelFactory

    private val newsComponentViewModel: ComponentViewModel<NewsListComponent> by activityViewModels {
        ComponentViewModel.createBy { helpComponent().build() }
    }
    private val filterComponentViewModel: ComponentViewModel<FilterComponent> by viewModels {
        ComponentViewModel.createBy(newsComponentViewModel.component) {
            filterComponent().build()
        }
    }
    private val filterViewModel: FilterViewModel by viewModels {
        createFactoryByViewModel { viewModelFactory.create<FilterViewModel>() }
    }
    private val compositeDisposable = CompositeDisposable()

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

        filterViewModel.state.observeOn(AndroidSchedulers.mainThread())
            .subscribe(::updateState)
            .also(compositeDisposable::add)

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
        compositeDisposable.clear()
    }
}