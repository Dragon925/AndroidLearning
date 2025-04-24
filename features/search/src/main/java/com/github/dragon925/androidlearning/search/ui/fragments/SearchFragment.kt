package com.github.dragon925.androidlearning.search.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewpager2.widget.ViewPager2
import com.github.dragon925.androidlearning.search.R
import com.github.dragon925.androidlearning.search.databinding.FragmentSearchBinding
import com.github.dragon925.androidlearning.search.ui.adapters.SearchViewPagerAdapter
import com.github.dragon925.androidlearning.search.ui.utils.textChanges
import com.github.dragon925.androidlearning.search.ui.viewmodels.SharedSearchViewModel
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


@OptIn(FlowPreview::class)
class SearchFragment : Fragment() {

    companion object {
        const val SAVED_QUERY = "SearchFragment-query"
    }

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SharedSearchViewModel by activityViewModels {
        SharedSearchViewModel.FACTORY
    }
    private var searchObserver: Job? = null

    private lateinit var searchViewPagerAdapter: SearchViewPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentSearchBinding.inflate(inflater, container, false)
        .also { _binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchViewPagerAdapter = SearchViewPagerAdapter(this)

        binding.viewPager.adapter = searchViewPagerAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.tab_by_event)
                1 -> tab.text = getString(R.string.tab_by_nko)
            }
        }.attach()

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                val hint = when (position) {
                    0 -> getString(R.string.search_hint_by_event)
                    1 -> getString(R.string.search_hint_by_organization)
                    else -> ""
                }
                binding.searchView.hint = hint
                binding.searchBar.hint = hint

            }
        })

        if (savedInstanceState != null && !savedInstanceState.isEmpty) {
            viewModel.search(savedInstanceState.getString(SAVED_QUERY, ""))
        }

        with(binding) {
            searchView.editText.setOnEditorActionListener { _, _, _ ->
                searchView.hide()
                false
            }
            searchObserver = viewModel.observeQuery(searchView.textChanges())

            lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.query.collect { searchBar.setText(it) }
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        val query = _binding?.let { it.searchView.editText.text?.toString() } ?: ""
        if (query.isNotEmpty()) {
            outState.putString(SAVED_QUERY, query)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        searchObserver?.cancel()
        searchObserver = null
    }
}