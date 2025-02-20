package com.github.dragon925.androidlearning.news.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.dragon925.androidlearning.databinding.ItemNewsFilterBinding
import com.github.dragon925.androidlearning.news.ui.models.FilterItem

class FilterListAdapter(
    private val checkFilter: (Int, Boolean) -> Unit
) : ListAdapter<FilterItem, FilterListAdapter.FilterItemViewHolder>(
    ItemDiffCallback()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = FilterItemViewHolder(
        ItemNewsFilterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: FilterItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class FilterItemViewHolder(
        private val binding: ItemNewsFilterBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: FilterItem) {
            with(binding.swFilter) {
                text = item.category.name
                isChecked = item.isChecked
                setOnCheckedChangeListener { _, isChecked ->
                    checkFilter(item.category.id, isChecked)
                }
            }
        }
    }

    private class ItemDiffCallback : DiffUtil.ItemCallback<FilterItem>() {
        override fun areItemsTheSame(
            oldItem: FilterItem,
            newItem: FilterItem
        ): Boolean = oldItem.category.id == newItem.category.id

        override fun areContentsTheSame(
            oldItem: FilterItem,
            newItem: FilterItem
        ): Boolean = oldItem == newItem
    }
}