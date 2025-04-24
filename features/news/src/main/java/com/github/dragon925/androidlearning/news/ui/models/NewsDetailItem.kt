package com.github.dragon925.androidlearning.news.ui.models


import com.github.dragon925.androidlearning.core.api.domain.models.Member

internal data class NewsDetailItem(
    val id: String,
    val name: String,
    val description: String,
    val organizer: String,
    val address: String,
    val phoneNumbers: List<String>,
    val email: String,
    val website: String,
    val date: String,
    val photos: List<String>,
    val members: List<Member> = emptyList()
)