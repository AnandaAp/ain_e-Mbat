package util

import com.ain.embat.BuildConfig

actual interface Navigator {
    actual fun navigate(tag: String)
}

actual fun geminiApiKey() = BuildConfig.geminiApiKey

actual interface GeminiInterface {
    actual val apiKey: String
}