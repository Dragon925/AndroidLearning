package com.github.dragon925.androidlearning.authorization.ui

import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

internal fun EditText.textChanges(): Flow<String> = callbackFlow {

    val watcher = this@textChanges.doAfterTextChanged { trySend(it.toString()) }

    awaitClose { this@textChanges.removeTextChangedListener(watcher) }
}