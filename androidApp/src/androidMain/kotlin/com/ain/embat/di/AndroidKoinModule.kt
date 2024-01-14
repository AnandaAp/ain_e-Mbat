package com.ain.embat.di

import android.content.Context
import com.ain.embat.utils.TensorFlowModeler
import com.ain.embat.viewmodel.NgelarasRecordViewModel
import org.koin.dsl.module

fun nativeAndroidKoinModule(context: Context) = module {
    factory { TensorFlowModeler(context) }
    factory { NgelarasRecordViewModel() }
    single { context }
}