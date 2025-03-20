package com.github.dragon925.androidlearning.search.ui.fragments

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
import androidx.fragment.app.viewModels
import androidx.lifecycle.DEFAULT_ARGS_KEY
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.dragon925.androidlearning.R
import com.github.dragon925.androidlearning.common.ui.UIState
import com.github.dragon925.androidlearning.databinding.FragmentSearchByTypeBinding
import com.github.dragon925.androidlearning.search.ui.adapters.SearchResultListAdapter
import com.github.dragon925.androidlearning.search.ui.models.SearchUIState
import com.github.dragon925.androidlearning.search.ui.viewmodels.SearchViewModel
import com.github.dragon925.androidlearning.search.ui.viewmodels.SharedSearchViewModel
import com.google.android.material.divider.MaterialDividerItemDecoration
import kotlinx.coroutines.launch

private const val SEARCH_TYPE = "searchType"

class SearchByTypeFragment : Fragment() {

    companion object {
        const val SEARCH_BY_EVENT = 1
        const val SEARCH_BY_NKO = 2

        @JvmStatic
        fun newInstance(searchType: Int) = SearchByTypeFragment().apply {
            arguments = Bundle().apply { putInt(SEARCH_TYPE, searchType) }
        }
    }

    private var searchType: Int? = null
    private var searchFragment: SearchFragment? = null

    private var _binding: FragmentSearchByTypeBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: SharedSearchViewModel by activityViewModels()
    private val searchViewModel: SearchViewModel by viewModels(
        extrasProducer = {
            MutableCreationExtras(defaultViewModelCreationExtras).apply {
                this[DEFAULT_ARGS_KEY] = bundleOf(
                    SearchViewModel.SEARCH_TYPE to (searchType ?: SEARCH_BY_EVENT)
                )
            }
        },
        factoryProducer = { SearchViewModel.Factory }
    )

    private lateinit var resultAdapter: SearchResultListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            searchType = it.getInt(SEARCH_TYPE)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        searchFragment = parentFragment as? SearchFragment
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentSearchByTypeBinding.inflate(inflater, container, false)
        .also { _binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        resultAdapter = SearchResultListAdapter()
        val divider = MaterialDividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
        divider.setDividerInsetStartResource(requireContext(), R.dimen.spacing_l)

        with(binding) {
            rvSearchResults.adapter = resultAdapter
            rvSearchResults.addItemDecoration(divider)
            tvSearchHelpLabel.text = getString(
                when (searchType) {
                    SEARCH_BY_NKO -> R.string.search_help_organization
                    else -> R.string.search_help_event
                }
            )
            tvExample.text = getString(
                when (searchType) {
                    SEARCH_BY_NKO -> R.string.search_example_organization
                    else -> R.string.search_example_event
                }
            )
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                sharedViewModel.query.collect { searchViewModel.search(it) }
            }
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                searchViewModel.viewState.collect(::updateState)
            }
        }
    }

    private fun updateState(state: UIState<SearchUIState, String>) {
        with(binding) {
            piLoading.isVisible = state.isLoading
            groupResults.isGone = !state.isCorrect
            groupPlug.isGone = state.isCorrect
        }
        if (state.isCorrect) {
            state.data?.let { showResults(it) }
        }
    }

    private fun showResults(data: SearchUIState) {
        val resultPluralsId = when (searchType) {
            SEARCH_BY_NKO -> R.plurals.search_result_organizations
            else -> R.plurals.search_result_events
        }
        with(binding) {
            tvSearchKeywords.text = getString(
                R.string.search_keywords, data.keywords.joinToString()
            )
            tvSearchResultCount.text = resources.getQuantityString(
                resultPluralsId, data.results.size, data.results.size
            )
            resultAdapter.submitList(data.results)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        searchFragment = null
    }
}