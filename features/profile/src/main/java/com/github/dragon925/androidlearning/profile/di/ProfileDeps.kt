package com.github.dragon925.androidlearning.profile.di

import android.content.Context
import com.github.dragon925.androidlearning.core.api.domain.models.FeatureDeps

interface ProfileDeps : FeatureDeps {

    val context: Context
}