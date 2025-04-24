package com.github.dragon925.androidlearning.common.ui

import com.github.dragon925.androidlearning.help.di.HelpDeps
import com.github.dragon925.androidlearning.news.di.NewsDeps
import com.github.dragon925.androidlearning.profile.di.ProfileDeps
import com.github.dragon925.androidlearning.search.di.SearchDeps

interface DepsHandler : HelpDeps, ProfileDeps, SearchDeps, NewsDeps