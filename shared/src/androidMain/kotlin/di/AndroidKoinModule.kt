package di

import ai.GeminiAI
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import viewmodel.StarterViewModel

fun androidKoinModule()= module {
    factory { GeminiAI().model }
    viewModel { StarterViewModel() }
}