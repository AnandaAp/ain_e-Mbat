package di

import ai.GeminiAI
import org.koin.dsl.module

fun androidKoinModule()= module {
    factory { GeminiAI().model }
}