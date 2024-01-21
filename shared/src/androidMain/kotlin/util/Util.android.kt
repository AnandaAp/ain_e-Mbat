package util

import com.ain.embat.BuildConfig
import dev.icerock.moko.permissions.PermissionsController

actual interface Navigator {
    actual fun navigate(tag: String)
}

actual fun geminiApiKey() = BuildConfig.geminiApiKey

actual interface GeminiInterface {
    actual val apiKey: String
}

val permission = PermissionsController::class