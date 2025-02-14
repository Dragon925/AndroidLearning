package com.github.dragon925.androidlearning.search.ui.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.github.dragon925.androidlearning.search.ui.fragments.SearchByTypeFragment

class SearchViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> SearchByTypeFragment.newInstance(SearchByTypeFragment.SEARCH_BY_EVENT)
        1 -> SearchByTypeFragment.newInstance(SearchByTypeFragment.SEARCH_BY_NKO)
        else -> throw IllegalArgumentException("Unknown position: $position")
    }
}