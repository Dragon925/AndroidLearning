package com.github.dragon925.androidlearning.profile.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.dragon925.androidlearning.core.api.domain.models.Mapper
import com.github.dragon925.androidlearning.core.api.ui.UIState
import com.github.dragon925.androidlearning.profile.domain.repositories.ProfileRepository
import com.github.dragon925.androidlearning.profile.domain.models.Profile
import com.github.dragon925.androidlearning.profile.ui.models.ProfileUIState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

internal class ProfileViewModel @AssistedInject constructor(
    @Assisted("profileId") private val profileId: String,
    private val repository: ProfileRepository,
    private val mapper: Mapper<Profile, ProfileUIState>
) : ViewModel() {

    private val loading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val profile = MutableStateFlow(ProfileUIState.EMPTY)

    val state: Flow<UIState<ProfileUIState, String>> = loading
        .combine(profile) { loading, profile ->
            UIState(
                isLoading = loading,
                data = profile.takeIf { it != ProfileUIState.EMPTY },
            )
        }

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            repository.loadProfile(profileId).onStart { loading.value = true }
                .map(mapper::invoke)
                .flowOn(Dispatchers.IO)
                .onEach { loading.value = false }
                .catch { error ->
                    Log.e("ProfileViewModel", "Error loading profile", error)
                }
                .collect {
                    profile.value = it
                }
        }
    }

    @AssistedFactory
    fun interface Factory {
        fun create(@Assisted("profileId") profileId: String): ProfileViewModel
    }
}