package constants

object AppConstant {
    const val PRODUCT = "Product"
    const val SHADY = "Shady"
    const val SHADY_SUBSYSTEM = "Shady Subsystem"
    const val PROTOTYPE = "PROTOTYPE"
    const val CACHE_KEY = "Ain E-MBAT Cached Key"
    const val CACHE_KEY_INIT = "Ain E-MBAT Cached Key initialize value"
    const val CACHE_KEY_INIT_KEY = "Ain E-MBAT Cached Key initialize key"
    const val ANDROID = "Android"
    const val IOS = "iOS"
    const val BOTTOM_NAV_LIST = "Bottom_Nav_List"
    object Type {
        const val ANDROID_ONLY = "ANDROID_ONLY"
        const val IOS_ONLY = "IOS_ONLY"
        const val FULL_TYPE = "FULL_TYPE"
    }
}

object RuntimeCacheConstant {
    const val APP_PRODUCT = "Application product"
    const val TRUE = "true"
    const val FALSE = "false"
}

object ProductConstant {
    const val TYPE = "Type"
    const val STATUS = "Status"
    const val VERSION = "Version"
}

object ExceptionConstant {
    const val PRODUCT_NOT_FOUND = "Product not found"
    const val ONLY_ELIGIBLE_FOR_ANDROID = "product is only eligible for android"
    const val ONLY_ELIGIBLE_FOR_IOS = "product is only eligible for android"
    const val NOT_ELIGIBLE = "product is not eligible"
    const val FULL_ELIGIBLE = "product is eligible for both"
    const val DATA_NOT_FOUND = "Data not found"
}

object Characters {
    const val EMPTY = ""
}

object Numbers {
    const val ZERO = 0
    const val ONE = 1
    const val TWO = 2
    const val THREE= 3
    const val FOUR = 4
    const val FIVE = 5
    const val SIX = 6
    const val SEVEN = 7
    const val EIGHT = 8
    const val NINE = 9
    const val TEN = 10
}

object SplashScreen {
    const val DIRECTED_BY = "Directed by:"
    const val AUTHOR = "Muhammad Ainun Zibran"
}

fun String.isNotNullOrEmpty(): Boolean = this.isNotBlank()
        && this.isNotEmpty()
        && this != Characters.EMPTY