package com.github.dragon925.androidlearning.help.di

import com.github.dragon925.androidlearning.help.ui.fragments.HelpCategoriesFragment
import dagger.Component

@Component(
    modules = [HelpModule::class],
    dependencies = [HelpDeps::class]
)
@HelpScope
internal fun interface HelpComponent {

    fun inject(helpCategoriesFragment: HelpCategoriesFragment)

    @Component.Builder
    interface Builder {

        fun deps(deps: HelpDeps): Builder

        fun build(): HelpComponent
    }
}