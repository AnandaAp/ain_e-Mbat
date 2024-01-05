package di

import constants.AppConstant.CACHE_KEY
import constants.AppConstant.CACHE_KEY_INIT
import constants.AppConstant.CACHE_KEY_INIT_KEY
import constants.ExceptionConstant.DATA_NOT_FOUND
import constants.RuntimeCacheConstant.FALSE
import constants.RuntimeCacheConstant.TRUE

@Suppress("UNCHECKED_CAST")
object RuntimeCache {
    private val cache: Map<String, Any> = hashMapOf(CACHE_KEY to mutableMapOf<String, Any>())

    private val runtimeCache: MutableMap<String, Any> =  cache[CACHE_KEY] as MutableMap<String, Any>

    init {
        if (getString(CACHE_KEY_INIT_KEY) != DATA_NOT_FOUND) {
            put(CACHE_KEY_INIT_KEY, CACHE_KEY_INIT)
        }
    }

    private fun String.isKeyNotEmpty(): Boolean = this.isNotBlank() && this.isNotEmpty()

    private fun String.isFoundInCache(): Boolean = this.isKeyNotEmpty()
            && runtimeCache.containsKey(this)

    private fun String.isBoolean(): Boolean = TRUE.equals(this, ignoreCase = true)
            || FALSE.equals(this, ignoreCase = true)

    fun <T : Any> put(key: String, value: T?): Boolean {
        if (key.isFoundInCache()) {
            remove(key)
        }
        if (key.isKeyNotEmpty() && value != null) {
            runtimeCache[key] = value
        }
        return key.isFoundInCache()
    }

    fun getString(key: String): String =
        if (key.isFoundInCache()) {
            runtimeCache[key].toString()
        } else {
            DATA_NOT_FOUND
        }

    fun <T> getList(key: String): List<T> =
        if (key.isFoundInCache()) {
            runtimeCache[key] as List<T>
        } else {
            listOf()
        }

    fun getBoolean(key: String): Boolean =
        if (key.isFoundInCache() && runtimeCache[key].toString().isBoolean()) {
            runtimeCache[key] as Boolean
        } else {
            false
        }

    fun remove(key: String): Boolean {
        if (key.isFoundInCache()) {
            runtimeCache.remove(key)
        }
        return !key.isFoundInCache()
    }

    fun clear() = runtimeCache.clear()

    fun get(key: String): Any? = if (key.isFoundInCache()) runtimeCache[key]!! else null
}