package com.github.dragon925.androidlearning.help.di

import com.github.dragon925.androidlearning.core.api.domain.models.FeatureDeps
import com.github.dragon925.androidlearning.core.api.domain.repositories.CategoryRepository

interface HelpDeps : FeatureDeps {

    val categoryRepository: CategoryRepository
}