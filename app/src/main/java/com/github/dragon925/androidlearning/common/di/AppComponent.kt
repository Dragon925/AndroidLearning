package com.github.dragon925.androidlearning.common.di

import android.content.Context
import com.github.dragon925.androidlearning.core.di.CoreComponent
import dagger.BindsInstance
import dagger.Component

@Component(
    modules = [AppModule::class],
    dependencies = [CoreComponent::class]
)
@AppScope
interface AppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun context(context: Context): Builder

        fun coreComponent(coreComponent: CoreComponent): Builder

        fun build(): AppComponent
    }
}