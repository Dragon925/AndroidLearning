package com.github.dragon925.androidlearning.common.data.repositories

import android.content.res.AssetManager
import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class LoadHelper<T>(
    private val fileName: String,
    private val jsonClass: Class<Array<T>>,
    private val gson: Gson = Gson()
) {

    fun <R> loadData(
        assets: AssetManager,
        loader: suspend () -> List<T>,
        mapper: (List<T>) -> List<R>
    ): Flow<List<R>> = flow {
        emit(loader())
    }
        .catch {
            emit(loadLocalData(assets))
        }
        .map(mapper)

    fun <R> loadFirstData(
        assets: AssetManager,
        loader: suspend () -> T,
        mapper: (T) -> R,
        predicate: (T) -> Boolean
    ): Flow<R> = flow {
        emit(loader())
    }
        .catch {
            emit(loadLocalData(assets).first(predicate))
        }
        .map(mapper)

    private suspend fun loadLocalData(
        assets: AssetManager,
        dispatcher: CoroutineDispatcher = Dispatchers.IO
    ): List<T> = withContext(dispatcher) {
        return@withContext try {
            assets.open(fileName).bufferedReader().use { reader ->
                gson.fromJson(reader.readText(), jsonClass).toList()
            }
        } catch (e: Exception) {
            Log.e("LoadHelper", "load local data from file $fileName failed", e)
            emptyList()
        }
    }
}