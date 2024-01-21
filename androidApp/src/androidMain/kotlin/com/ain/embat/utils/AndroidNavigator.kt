package com.ain.embat.utils

import android.content.Context
import android.content.Intent
import com.ain.embat.ui.ngelaras.NgelarasActivity
import constants.NavigationConstant
import util.Navigator

class AndroidNavigator(private val context: Context): Navigator {
    override fun navigate(tag: String) {
        when(tag) {
            NavigationConstant.NGELARAS_LANSCAPE_ACTIVITY -> {
                val intent = Intent(context, NgelarasActivity::class.java)
                context.startActivities(arrayOf(intent))
            }
            else -> Unit
        }
    }
}