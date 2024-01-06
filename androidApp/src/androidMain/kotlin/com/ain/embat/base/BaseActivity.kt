package com.ain.embat.base

import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.core.view.WindowCompat
import com.ain.embat.ui.theme.Material3AinEmbatTheme
import com.google.ai.client.generativeai.GenerativeModel
import constants.AppConstant
import di.RuntimeCache
import moe.tlaster.precompose.PreComposeApp
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import timber.log.Timber

open class BaseActivity: ComponentActivity(), KoinComponent {
    protected val TAG = this::class.simpleName
    protected val cache: RuntimeCache by inject()
    protected val model: GenerativeModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.tag(TAG).e("$TAG is now on state: onCreate")
        Timber.tag(TAG).d(cache.getString(AppConstant.CACHE_KEY_INIT_KEY))
        setContent {
            PreComposeApp {
                Material3AinEmbatTheme {
                    InitiateUI()
                }
            }
        }
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
    protected open fun InitiateUI() {
        ConfigureScreenOrientation()
    }

    @Composable
    protected open fun ConfigureScreenOrientation() = Unit

    protected fun hideSystemUI() {
        actionBar?.hide()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            window.addFlags(WindowManager.LayoutParams.TYPE_STATUS_BAR)
        } else {
            window.insetsController?.apply {
                hide(WindowInsets.Type.statusBars())
                systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }
    }
}