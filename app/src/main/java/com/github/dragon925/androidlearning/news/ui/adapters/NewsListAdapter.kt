package com.github.dragon925.androidlearning.news.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.dragon925.androidlearning.databinding.ItemNewsBinding
import com.github.dragon925.androidlearning.news.ui.models.NewsItem

class NewsListAdapter(
    private val openDetails: (NewsItem) -> Unit
) : ListAdapter<NewsItem, NewsListAdapter.NewsItemViewHolder>(
    ItemDiffCallback(),
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = NewsItemViewHolder(
        ItemNewsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: NewsItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class NewsItemViewHolder(
        private val binding: ItemNewsBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: NewsItem) {
            with(binding) {
                tvTitle.text = item.title
                tvDescription.text = item.description
                btnDate.text = item.date
                btnDate.setOnClickListener { openDetails(item) }
                ivImage.visibility = if (item.image == null) View.GONE else View.VISIBLE
                item.image?.let { ivImage.setImageDrawable(it) }
            }
        }
    }

    private class ItemDiffCallback : DiffUtil.ItemCallback<NewsItem>() {

        override fun areItemsTheSame(
            oldItem: NewsItem,
            newItem: NewsItem
        ): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: NewsItem,
            newItem: NewsItem
        ): Boolean = oldItem == newItem
    }
}