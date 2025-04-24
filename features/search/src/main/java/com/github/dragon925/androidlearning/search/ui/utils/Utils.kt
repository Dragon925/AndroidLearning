package com.github.dragon925.androidlearning.search.ui.utils

import androidx.core.widget.doAfterTextChanged
import com.google.android.material.search.SearchView
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

internal fun SearchView.textChanges(): Flow<String> = callbackFlow {
    val editText = this@textChanges.editText

    val watcher = editText.doAfterTextChanged { trySend(it.toString()) }

    awaitClose { editText.removeTextChangedListener(watcher) }
}