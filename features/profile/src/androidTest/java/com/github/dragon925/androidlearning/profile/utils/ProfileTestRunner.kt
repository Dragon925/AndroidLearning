package com.github.dragon925.androidlearning.profile.utils

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner

class ProfileTestRunner : AndroidJUnitRunner() {

    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ): Application {
        return super.newApplication(cl, TestApp::class.java.name, context)
    }
}