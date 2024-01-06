package com.ain.embat.ui.ngelaras

import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.ain.embat.base.BaseActivity
import ui.ngelaras.BaseLandscapeNgelaras

class NgelarasActivity: BaseActivity() {

    override fun onDestroy() {
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        super.onDestroy()
    }

    @Composable
    override fun InitiateUI() {
        super.InitiateUI()
        BaseLandscapeNgelaras()
        hideSystemUI()
    }

    @Composable
    override fun ConfigureScreenOrientation() {
        val activity = LocalContext.current as Activity
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }
}