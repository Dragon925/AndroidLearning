package com.github.dragon925.androidlearning.search.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.dragon925.androidlearning.R
import com.github.dragon925.androidlearning.databinding.FragmentSearchByTypeBinding
import com.github.dragon925.androidlearning.search.ui.adapters.SearchResultListAdapter
import com.github.dragon925.androidlearning.search.ui.models.SearchResultItem
import com.google.android.material.divider.MaterialDividerItemDecoration

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

    private var _binding: FragmentSearchByTypeBinding? = null
    private val binding get() = _binding!!

    private lateinit var resultAdapter: SearchResultListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            searchType = it.getInt(SEARCH_TYPE)
        }
    }

    override fun onResume() {
        super.onResume()
        updateData()
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
            tvSearchInfo.text = generateSearchInfo()
            rvSearchResults.adapter = resultAdapter
            rvSearchResults.addItemDecoration(divider)
        }
        resultAdapter.submitList(generateNewData())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateData() {
        resultAdapter.submitList(generateNewData())
    }

    private fun generateSearchInfo() = "Ключевые слова: мастер-класс, помощь\nРезультаты поиска: 10 мероприятий"

    private fun generateNewData(): List<SearchResultItem> = List(10) {
        SearchResultItem(it + 1, generateRandomString())
    }

    private fun generateRandomString(): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..10)
            .map { allowedChars.random() }
            .joinToString("")
    }
}