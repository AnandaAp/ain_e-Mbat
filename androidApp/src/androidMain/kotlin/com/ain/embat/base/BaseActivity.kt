package com.ain.embat.base

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import constants.AppConstant
import di.RuntimeCache
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import timber.log.Timber

open class BaseActivity: ComponentActivity(), KoinComponent {
    protected val TAG = this::class.simpleName
    protected val runtimeCache: RuntimeCache by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.tag(TAG).e("$TAG is now on state: onCreate")
        Timber.tag(TAG).d(runtimeCache.getString(AppConstant.CACHE_KEY_INIT_KEY))
    }

    override fun onDestroy() {
//        runtimeCache.clear()
        Timber.tag(TAG).e("$TAG is now on state: onDestroy")
        super.onDestroy()
    }

    override fun onStop() {
        Timber.tag(TAG).e("$TAG is now on state: onStop")
        super.onStop()
    }

    override fun onStart() {
        Timber.tag(TAG).e("$TAG is now on state: onStart")
        super.onStart()
    }

    override fun onPause() {
        Timber.tag(TAG).e("$TAG is now on state: onPause")
        super.onPause()
    }

    @Composable
    protected open fun InitiateUI() = Unit
}