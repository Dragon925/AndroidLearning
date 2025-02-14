package com.github.dragon925.androidlearning.search.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.dragon925.androidlearning.R
import com.github.dragon925.androidlearning.databinding.FragmentSearchBinding
import com.github.dragon925.androidlearning.search.ui.adapters.SearchViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator


class SearchFragment : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance() = SearchFragment()
    }

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

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
            when(position) {
                0 -> tab.text = getString(R.string.tab_by_event)
                1 -> tab.text = getString(R.string.tab_by_nko)
            }
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}