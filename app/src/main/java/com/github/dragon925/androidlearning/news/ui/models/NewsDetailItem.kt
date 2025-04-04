package com.github.dragon925.androidlearning.news.ui.models


import android.os.Parcelable
import com.github.dragon925.androidlearning.common.domain.Member
import kotlinx.parcelize.Parcelize

@Parcelize
data class NewsDetailItem(
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
) : Parcelable