package com.github.dragon925.androidlearning.profile.utils

import android.app.Application
import android.content.Context
import com.github.dragon925.androidlearning.profile.di.ProfileDeps

class TestApp : Application(), ProfileDeps {

    override val context: Context
        get() = this
}