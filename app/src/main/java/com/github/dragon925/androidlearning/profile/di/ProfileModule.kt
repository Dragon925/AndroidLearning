package com.github.dragon925.androidlearning.profile.di

import android.content.Context
import com.github.dragon925.androidlearning.common.contract.Mapper
import com.github.dragon925.androidlearning.profile.data.repositories.ProfileRepositoryImpl
import com.github.dragon925.androidlearning.profile.domain.repositories.ProfileRepository
import com.github.dragon925.androidlearning.profile.domain.models.Profile
import com.github.dragon925.androidlearning.profile.ui.models.ProfileUIState
import com.github.dragon925.androidlearning.profile.ui.toProfileState
import dagger.Module
import dagger.Provides

@Module
object ProfileModule {

    @Provides
    @ProfileScope
    fun provideProfileRepository(): ProfileRepository = ProfileRepositoryImpl

    @Provides
    fun provideProfileItemMapper(context: Context): Mapper<Profile, ProfileUIState> {
        return Mapper { it.toProfileState(context) }
    }
}