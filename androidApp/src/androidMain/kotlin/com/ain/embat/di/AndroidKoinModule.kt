package com.ain.embat.di

import android.content.Context
import com.ain.embat.viewmodel.NgelarasRecordViewModel
import org.koin.dsl.module

fun nativeAndroidKoinModule(context: Context) = module {
    single { context }

    // Use factory instead of single when user presses back button...
    // to force execution of init block when interpreter is closed
    factory { NgelarasRecordViewModel() }
}