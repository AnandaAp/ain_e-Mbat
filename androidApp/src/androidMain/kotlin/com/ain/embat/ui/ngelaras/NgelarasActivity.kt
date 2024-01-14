package com.ain.embat.ui.ngelaras

import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.ain.embat.base.BaseActivity
import com.ain.embat.navigation.NgelarasRecordNav

class NgelarasActivity: BaseActivity() {

    override fun onDestroy() {
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        super.onDestroy()
    }

    @Composable
    override fun InitiateUI() {
        super.InitiateUI()
        hideSystemUI()
        NgelarasRecordNav()
    }

    @Composable
    override fun ConfigureScreenOrientation() {
        val activity = LocalContext.current as Activity
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }
}

@Preview
@Composable
fun PreviewNgelarasRecord() {
    NgelarasRecordNav()
}