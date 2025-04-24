package com.github.dragon925.androidlearning.help.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import com.github.dragon925.androidlearning.help.databinding.ItemHelpCategoryBinding
import com.github.dragon925.androidlearning.help.ui.models.HelpCategoryItem

internal class HelpCategoryListAdapter : ListAdapter<HelpCategoryItem, HelpCategoryListAdapter.HelpCategoryViewHolder>(
    ItemDiffCallback(),
) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = HelpCategoryViewHolder (
        ItemHelpCategoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: HelpCategoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class HelpCategoryViewHolder(
        private val binding: ItemHelpCategoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: HelpCategoryItem) {
            with(binding) {
                tvTitle.text = item.title
                ivAvatar.load(item.icon)
            }
        }
    }

    private class ItemDiffCallback : DiffUtil.ItemCallback<HelpCategoryItem>() {

        override fun areItemsTheSame(
            oldItem: HelpCategoryItem,
            newItem: HelpCategoryItem
        ): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: HelpCategoryItem,
            newItem: HelpCategoryItem
        ): Boolean = oldItem == newItem
    }
}