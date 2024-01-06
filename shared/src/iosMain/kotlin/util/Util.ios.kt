package util

import constants.AppConstant

actual interface Navigator {
    actual fun navigate(tag: String)
}

actual fun geminiApiKey() = AppConstant.DEFAULT_STRING_VALUE

actual interface GeminiInterface {
    actual val apiKey: String
}