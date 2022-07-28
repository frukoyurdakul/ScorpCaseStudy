package com.scorp.casestudy.furkanyurdakul.util

import android.util.Log
import com.scorp.casestudy.furkanyurdakul.BuildConfig

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

object TaskDispatchers
{
    private const val TAG = "TaskDispatchers"

    // Root coroutine context that contains a supervisor job and exception handler.
    private val rootContext = SupervisorJob() + CoroutineExceptionHandler { _, t ->
        if (BuildConfig.DEBUG)
            Log.e(TAG, "Exception while executing coroutine.", t)
    }

    // Merged scopes with dispatchers (named with first letter uppercase
    // to follow Dispatchers class)
    val MainScope = CoroutineScope(Dispatchers.Main.immediate + rootContext)
    val IOScope = CoroutineScope(Dispatchers.IO + rootContext)
    val DefaultScope = CoroutineScope(Dispatchers.Default + rootContext)
}