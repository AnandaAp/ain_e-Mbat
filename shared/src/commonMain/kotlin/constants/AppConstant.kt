package constants

import androidx.compose.ui.unit.dp

object AppConstant {
    const val PRODUCT = "Product"
    const val SHADY = "Shady"
    const val SHADY_SUBSYSTEM = "Shady Subsystem"
    const val PROTOTYPE = "PROTOTYPE"
    const val CACHE_KEY = "Ain E-MBAT Cached Key"
    const val CACHE_KEY_INIT = "Ain E-MBAT Cached Key initialize value"
    const val CACHE_KEY_INIT_KEY = "Ain E-MBAT Cached Key initialize key"
    const val DAFTAR_GAMELAN_TITLE = "daftar gamelan"
    const val ANDROID = "Android"
    const val IOS = "iOS"
    const val BOTTOM_NAV_LIST = "Bottom_Nav_List"
    const val DEFAULT_STRING_VALUE = Characters.EMPTY
    const val DEFAULT_GAMELAN_VALUE = "Gamelan Doe"
    const val DEFAULT_NUMBER_OF_RECORD = 1
    const val DEFAULT_INTEGER_VALUE = 0
    const val DEFAULT_FLOAT_VALUE = 0f
    const val DEFAULT_BOOLEAN_VALUE = false
    const val NAME = "name"
    const val UNIQUE = "unique"
    const val LIST_OF_GAMELAN_TOP_TITLE = "Daftar Gamelan Dari\n"
    object Type {
        const val ANDROID_ONLY = "ANDROID_ONLY"
        const val IOS_ONLY = "IOS_ONLY"
        const val FULL_TYPE = "FULL_TYPE"
    }
}

object RuntimeCacheConstant {
    const val APP_PRODUCT = "Application product"
    const val CATEGORY_OF_GAMELAN_KEY = "CategorY 0F Gamel@n R3trieven From F1re5t0r3"
    const val GAMELAN_KEY = "Gamel@n R3trieven From F1re5t0r3"
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
    const val AUTHOR = "Muhamad Ainun Zibran"
}

object DefaultPadding {
    val DEFAULT_ALL = 14.dp
    val DEFAULT_WIDTH = 14.dp
    val DEFAULT_HEIGHT = 14.dp
    val DEFAULT_GRID_MIN_SIZE = 128.dp
    val DEFAULT_CONTENT_HORIZONTAL_PADDING = 10.dp
    val DEFAULT_CONTENT_VERTICAL_PADDING = 10.dp
    val DEFAULT_CONTENT_PADDING_ALL = 10.dp
}

object DefaultSize {
    const val DEFAULT_ROUNDED_CORNER_PERCENTAGE = 25
    const val DEFAULT_ROUNDED_CORNER_FLOAT = .25f
}

object DefaultViewModel {
    const val DEFAULT_KEY_VALUE = 19280081
    const val DEFAULT_SAVED_VALUE_KEY = "DEf@ul7 54v3d v@Lu3"
}

object DefaultTensorFlow {
    const val BYTE_OUTPUT_ID = 683
    const val WAV_ID = 233
    const val FRAME_SIZE = 80
}

object ResourceDefault {
    const val DEFAULT_RECORD_ICON_CONTENT_DESCRIPTION = "Record The sound"
    const val DEFAULT_RECORD_TITLE = "Start Recording The Gamelan"
}