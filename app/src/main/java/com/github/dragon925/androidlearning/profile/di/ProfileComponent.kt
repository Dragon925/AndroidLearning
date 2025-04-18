package com.github.dragon925.androidlearning.profile.di

import com.github.dragon925.androidlearning.profile.ui.fragments.ProfileFragment
import dagger.Subcomponent

@Subcomponent(modules = [ProfileModule::class])
@ProfileScope
fun interface ProfileComponent {

    fun inject(profileFragment: ProfileFragment)

    @Subcomponent.Builder
    fun interface Builder{

        fun build(): ProfileComponent
    }
}