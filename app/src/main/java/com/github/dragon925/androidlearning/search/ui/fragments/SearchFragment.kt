package com.github.dragon925.androidlearning.search.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.github.dragon925.androidlearning.R
import com.github.dragon925.androidlearning.databinding.FragmentSearchBinding
import com.github.dragon925.androidlearning.search.ui.adapters.SearchViewPagerAdapter
import com.github.dragon925.androidlearning.search.ui.viewmodels.SharedSearchViewModel
import com.google.android.material.tabs.TabLayoutMediator
import com.jakewharton.rxbinding4.widget.textChanges
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit


class SearchFragment : Fragment() {

    companion object {
        const val SAVED_QUERY = "SearchFragment-query"

        @JvmStatic
        fun newInstance() = SearchFragment()
    }

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SharedSearchViewModel by activityViewModels()
    private val compositeDisposable = CompositeDisposable()

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

            searchView.editText.textChanges().map { it.toString().trim() }
                .debounce(500, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { viewModel.search(it) }
                .also { compositeDisposable.add(it) }

            viewModel.query.observeOn(AndroidSchedulers.mainThread())
                .subscribe { searchBar.setText(it) }
                .also { compositeDisposable.add(it) }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        val query = binding.searchView.editText.text.toString().trim()
        if (query.isNotEmpty()) {
            outState.putString(SAVED_QUERY, query)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        compositeDisposable.clear()
    }
}