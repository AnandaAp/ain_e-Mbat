package com.ain.embat.ui.ngelaras

import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.ain.embat.base.BaseActivity
import com.ain.embat.ui.theme.Material3AinEmbatTheme

class NgelarasActivity: BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Material3AinEmbatTheme {
                InitiateUI()
            }
        }
    }

    override fun onDestroy() {
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        super.onDestroy()
    }

    @Composable
    override fun InitiateUI() {
        super.InitiateUI()
    }

    @Composable
    override fun ConfigureScreenOrientation() {
        val activity = LocalContext.current as Activity
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }
}