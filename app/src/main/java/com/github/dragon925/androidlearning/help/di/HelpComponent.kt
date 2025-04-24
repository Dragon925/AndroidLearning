package com.github.dragon925.androidlearning.help.di

import com.github.dragon925.androidlearning.help.ui.fragments.HelpCategoriesFragment
import dagger.Subcomponent

@Subcomponent(modules = [HelpModule::class])
@HelpScope
fun interface HelpComponent {

    fun inject(helpCategoriesFragment: HelpCategoriesFragment)

    @Subcomponent.Builder
    fun interface Builder {

        fun build(): HelpComponent
    }
}