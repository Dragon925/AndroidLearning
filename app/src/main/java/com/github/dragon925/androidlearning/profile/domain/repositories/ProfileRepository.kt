package com.github.dragon925.androidlearning.profile.domain.repositories

import com.github.dragon925.androidlearning.profile.domain.models.Profile
import kotlinx.coroutines.flow.Flow

fun interface ProfileRepository {

    fun loadProfile(id: String): Flow<Profile>
}