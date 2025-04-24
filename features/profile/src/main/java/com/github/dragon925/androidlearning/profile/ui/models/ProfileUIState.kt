package com.github.dragon925.androidlearning.profile.ui.models

data class ProfileUIState(
    val id: String,
    val name: String,
    val birthday: String,
    val fieldOfActivity: String = "",
    val avatar: String = "",
    val friends: List<FriendItem> = emptyList()
) {
    companion object {
        val EMPTY = ProfileUIState(
            id = "",
            name = "",
            birthday = "",
        )
    }
}
