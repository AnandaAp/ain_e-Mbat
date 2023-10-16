package com.ain.embat.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import constants.AppConstant
import di.RuntimeCache
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import timber.log.Timber

open class BaseActivity: AppCompatActivity(), KoinComponent {
    protected val TAG = this::class.simpleName
    protected val runtimeCache: RuntimeCache by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.tag(TAG).d(runtimeCache.getString(AppConstant.CACHE_KEY_INIT_KEY))
    }

    override fun onDestroy() {
        runtimeCache.clear()
        super.onDestroy()
    }
}