package viewmodel

import constants.AppConstant
import constants.DefaultViewModel
import di.RuntimeCache
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import moe.tlaster.precompose.stateholder.SavedStateHolder
import org.koin.core.component.inject
import util.isNotNull

open class CommonPreComposeViewModel: PreComposeViewModel() {
    protected var key = DefaultViewModel.DEFAULT_KEY_VALUE
    protected lateinit var savedStateHolder: SavedStateHolder
    protected val baseSavedValue = MutableStateFlow(
        savedStateHolder
            .consumeRestored(DefaultViewModel.DEFAULT_SAVED_VALUE_KEY) as String?
            ?: AppConstant.DEFAULT_STRING_VALUE
    )
    protected val cache: RuntimeCache by inject()
    val savedValue = baseSavedValue.asStateFlow()

    protected fun Builder(
        key: Int,
        savedStateHolder: SavedStateHolder
    ): CommonPreComposeViewModel {
        val instance = CommonPreComposeViewModel()
        instance.key = key
        instance.savedStateHolder = savedStateHolder
        instance.savedStateHolder.registerProvider(DefaultViewModel.DEFAULT_SAVED_VALUE_KEY) {
            savedValue.value
        }
        return instance
    }

    fun updateKey(value: Int) {
        key = value
    }

    fun updateSavedStateHolder(stateHolder: SavedStateHolder) {
        if (stateHolder.isNotNull()) {
            savedStateHolder = stateHolder
        }
    }

    fun updateSavedStringValue(value: String) {
        baseSavedValue.update { value }
    }
}