package com.github.dragon925.androidlearning.profile.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.dragon925.androidlearning.profile.ui.models.FriendItem
import com.github.dragon925.androidlearning.databinding.ItemFriendBinding

class FriendsListAdapter : ListAdapter<FriendItem, FriendsListAdapter.FriendViewHolder>(
        ItemDiffCallback(),
) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ) = FriendViewHolder(
        ItemFriendBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false,
        ),
    )

    override fun onBindViewHolder(
        holder: FriendViewHolder,
        position: Int,
    ) {
        holder.bind(getItem(position))
    }

    inner class FriendViewHolder(
        private val binding: ItemFriendBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FriendItem) {
            binding.ivAvatar.setImageResource(item.avatarResId)
            binding.tvName.text = item.name
        }
    }

    private class ItemDiffCallback : DiffUtil.ItemCallback<FriendItem>() {
        override fun areItemsTheSame(
            oldItem: FriendItem,
            newItem: FriendItem,
        ): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: FriendItem,
            newItem: FriendItem,
        ): Boolean = oldItem == newItem
    }
}
