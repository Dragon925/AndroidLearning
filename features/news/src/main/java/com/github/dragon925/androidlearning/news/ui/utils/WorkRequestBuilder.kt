package com.github.dragon925.androidlearning.news.ui.utils

import androidx.work.Constraints
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.Worker
import androidx.work.workDataOf

internal inline fun <reified W: Worker> createOneTimeWorkRequest(
    vararg workRequestBuilderParameters: Pair<String, Any>,
    addConstrains: Constraints.Builder.() -> Constraints.Builder = { this },
    addExtras: OneTimeWorkRequest.Builder.() -> OneTimeWorkRequest.Builder = { this }
): OneTimeWorkRequest {
    val constraints = Constraints.Builder().addConstrains().build()
    val data = workDataOf(*workRequestBuilderParameters)

    return OneTimeWorkRequestBuilder<W>()
        .addTag(W::class.java.simpleName)
        .setConstraints(constraints)
        .setInputData(data)
        .addExtras()
        .build()
}