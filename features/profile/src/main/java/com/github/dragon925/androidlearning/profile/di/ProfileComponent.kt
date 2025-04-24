package com.github.dragon925.androidlearning.profile.di

import com.github.dragon925.androidlearning.profile.ui.fragments.ProfileFragment
import dagger.Component

@Component(modules = [ProfileModule::class], dependencies = [ProfileDeps::class])
@ProfileScope
internal fun interface ProfileComponent {

    fun inject(profileFragment: ProfileFragment)

    @Component.Builder
    interface Builder{

        fun deps(deps: ProfileDeps): Builder

        fun build(): ProfileComponent
    }
}