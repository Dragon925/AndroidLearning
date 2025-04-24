package com.github.dragon925.androidlearning.profile.domain.models

import kotlinx.datetime.LocalDate

internal data class Profile(
    val id: String,
    val name: String,
    val birthday: LocalDate,
    val fieldOfActivity: String = "",
    val avatar: String = "",
    val friends: List<Profile> = emptyList(),
)
