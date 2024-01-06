package viewmodel

import constants.AppConstant
import constants.FirestoreConstant
import constants.NavigationConstant
import constants.RuntimeCacheConstant
import dev.gitlive.firebase.FirebaseException
import di.RuntimeCache
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import models.CardModel
import models.CategoryOfGamelan
import models.Gamelan
import models.GamelanFrequency
import org.koin.core.component.inject
import states.AinAnimationState
import states.Status
import util.Navigator
import util.dummyCategoryOfGamelan
import util.dummyGamelanList
import util.isNotNullOrEmpty

class NgelarasViewModel: BaseViewModel() {
    private val firestoreCategoriesOfGamelan = MutableStateFlow(mutableListOf<CategoryOfGamelan>())
    private val baseSelectedCategoryOfGamelan = MutableStateFlow(CategoryOfGamelan())
    private val baseListOfGamelan = MutableStateFlow(mutableListOf<Gamelan>())
    private val baseCategoryCardModelOfGamelan = MutableStateFlow(mutableListOf<CardModel>())
    private val baseSelectedGamelan = MutableStateFlow(Gamelan())
    private val shimmerAnimateState = MutableStateFlow(AinAnimationState.Keep)

    val cache: RuntimeCache by inject()
    val selectedCategoryOfGamelan = baseSelectedCategoryOfGamelan.asStateFlow()
    val retrievedCategoriesOfGamelan = firestoreCategoriesOfGamelan.asStateFlow()
    val retrievedListOfGamelan = baseListOfGamelan.asStateFlow()
    val retrievedCardModel = baseCategoryCardModelOfGamelan.asStateFlow()
    val selectedGamelan = baseSelectedGamelan.asStateFlow()
    val animateState = shimmerAnimateState.asStateFlow()

    fun computeCardOnClick(
        selectedItem: Any,
        cachedKey: String = AppConstant.DEFAULT_STRING_VALUE,
        status: (Status) -> Unit = {}
    ) {
        viewModelScope.launch {
            when (selectedItem) {
                is CategoryOfGamelan -> {
                    if (selectedItem.isNotNullOrEmpty()) {
                        baseSelectedCategoryOfGamelan.update { selectedItem }
                        shimmerAnimateState.update { AinAnimationState.KeepAndSkip }
                    }
                }
                is Gamelan -> {
                    if (selectedItem.isNotNullOrEmpty()) {
                        baseSelectedGamelan.update { selectedItem }
                    }
                }
            }
            status (
                if (cache.put(key = cachedKey, value = selectedItem)) {
                    Status.Success
                }
                else Status.Failed
            )
        }
    }

    private suspend fun getListOfGamelan(): List<Gamelan> {
        try {
            val response = firestore
                .collection(FirestoreConstant.GAMELAN)
                .get()
            return if (response.isNotNullOrEmpty()) {
                val list = mutableListOf<Gamelan>()
                response
                    .documents
                    .filter { filter ->
                        filter.get<String>(field = AppConstant.UNIQUE) == baseSelectedCategoryOfGamelan.value.unique
                    }
                    .forEach {
                        val map = it.get<GamelanFrequency>(field = FirestoreConstant.FREQUENCY)
                        val name = it.get<String>(field = AppConstant.NAME)
                        val unique = it.get<String>(field = AppConstant.UNIQUE)
                        list.add(Gamelan(
                            name = name,
                            unique = unique,
                            frequency = map
                        ))
                    }
                list
            } else {
                dummyGamelanList()
            }
        } catch (e: FirebaseException) {
            throw e
        }
    }

    fun fetchListOfGamelan() {
        coroutine2.launch {
//            if (cache.getList<Gamelan>(key = RuntimeCacheConstant.GAMELAN_KEY).isNotNullOrEmpty()) {
//                return@launch
//            }
            delay(2000)
            val response = async {
                val retrievedGamelan = getListOfGamelan()
                println("retrieved gamelan: $retrievedGamelan")
                return@async retrievedGamelan
            }.await()
            if (response.isNotNullOrEmpty()) {
                baseListOfGamelan.update { response.toMutableList()  }
            }
            if (retrievedListOfGamelan.value.isNotNullOrEmpty() && cache.put(
                    key = RuntimeCacheConstant.GAMELAN_KEY,
                    value = retrievedListOfGamelan.value
                )) {
                baseCategoryCardModelOfGamelan.update {
                    val cards = mutableListOf<CardModel>()
                    retrievedListOfGamelan.value.forEach { gams ->
                        cards.add(CardModel(title = gams.name))
                    }
                    cards
                }
            }
            if (cache.getList<Gamelan>(key = RuntimeCacheConstant.GAMELAN_KEY).isNotNullOrEmpty()) {
                shimmerAnimateState.update { AinAnimationState.Hide }
            }
        }
    }

    override suspend fun fetch() {
        fetchListCategoryOfGamelan()
    }

    private suspend fun fetchListCategoryOfGamelan() {
        val channel = Channel<Boolean>()
        viewModelScope.launch(Dispatchers.IO) {
            if (cache.getList<Gamelan>(key = RuntimeCacheConstant.CATEGORY_OF_GAMELAN_KEY).isNotNullOrEmpty()) {
                return@launch
            }

            delay(2000)
            val response = async {
                return@async getListOfCategoryOfGamelan()
            }.await()

            if (response.isNotNullOrEmpty()) {
                val categories = mutableListOf<CategoryOfGamelan>()
                response.forEach { gamelan ->
                    categories.add(gamelan)
                }
                firestoreCategoriesOfGamelan.update { categories }
            }

            val isDataSaved = retrievedCategoriesOfGamelan.value.isNotNullOrEmpty() && cache.put(
                key = RuntimeCacheConstant.CATEGORY_OF_GAMELAN_KEY,
                value = retrievedCategoriesOfGamelan.value
            )

            if (isDataSaved) {
                channel.send(isDataSaved)
            }
            channel.close()
        }
        transformAnimatedState(channel)
    }

    private suspend fun transformAnimatedState(channel: Channel<Boolean>) {
        if (channel.receive()) {
            shimmerAnimateState.update { AinAnimationState.Hide }
            channel.close()
        }
    }

    private suspend fun getListOfCategoryOfGamelan(): List<CategoryOfGamelan> {
        try {
            val response = firestore
                .collection(FirestoreConstant.CATEGORIES_OF_GAMELAN)
                .get()
            return if (response.isNotNullOrEmpty()) {
                val list = mutableListOf<CategoryOfGamelan>()
                response.documents.forEach {
                    list.add(CategoryOfGamelan(
                        name = it.get(field = AppConstant.NAME),
                        unique = it.get(field = AppConstant.UNIQUE)
                    ))
                }
                list
            } else {
                dummyCategoryOfGamelan()
            }
        } catch (e: FirebaseException) {
            throw e
        }
    }

    fun checkAnimatedState(key: String) {
        viewModelScope.launch {
            if (animateState.value == AinAnimationState.Keep && cache.getList<Gamelan>(key = key).isNotNullOrEmpty()) {
                shimmerAnimateState.update { AinAnimationState.Hide }
            }
        }
    }

    fun navigateToLandscapeNavigator(navigator: Navigator) {
        navigator.navigate(NavigationConstant.NGELARAS_LANSCAPE_ACTIVITY)
    }
}