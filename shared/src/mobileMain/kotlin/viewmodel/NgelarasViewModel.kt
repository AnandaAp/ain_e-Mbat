package viewmodel

import constants.AppConstant
import constants.FirestoreConstant
import constants.NgelarasConstant
import constants.RuntimeCacheConstant
import constants.dummyGamelanList
import constants.isNotNullOrEmpty
import dev.gitlive.firebase.FirebaseException
import di.RuntimeCache
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import models.Gamelan
import org.koin.core.component.inject
import states.Status
import util.isNotNullOrEmpty

class NgelarasViewModel: BaseViewModel() {
    private val firestoreGamelan = MutableStateFlow(mutableListOf<Gamelan>())
    private val baseGamelan = MutableStateFlow(Gamelan())
    val cache: RuntimeCache by inject()
    val gamelan = baseGamelan.asStateFlow()
    val retrievedGamelan = firestoreGamelan.asStateFlow()

    fun computeGamelan(selectedGamelan: Gamelan, status: (Status) -> Unit = {}) {
        viewModelScope.launch {
            if (selectedGamelan.isNotNullOrEmpty()) {
                baseGamelan.update { selectedGamelan }
                status (
                    if (cache.put(key = NgelarasConstant.NGELARAS_SELECTED_GAMELAN, value = selectedGamelan))  Status.Success
                    else Status.Failed
                )
            }
        }
    }

    override suspend fun fetch() {
        fetchGamelan()
    }

    private fun fetchGamelan() {
        if (cache.getList<Gamelan>(key = RuntimeCacheConstant.GAMELAN_KEY).isNotNullOrEmpty()) {
            return
        }
        coroutine1.launch {
            val response = async {
                return@async getGamelanList()
            }.await()
            if (response.isNotNullOrEmpty()) {
                response.forEach { gamelan ->
                    firestoreGamelan.value.add(gamelan)
                }
            }
            if (retrievedGamelan.value.isNotNullOrEmpty()) {
                cache.put(key = RuntimeCacheConstant.GAMELAN_KEY, value = retrievedGamelan.value)
            }
        }
    }

    private suspend fun getGamelanList(): List<Gamelan> {
        try {
            val response = firestore
                .collection(FirestoreConstant.GAMELAN)
                .get()
            return if (response.isNotNullOrEmpty()) {
                val list = mutableListOf<Gamelan>()
                response.documents.forEach {
                    list.add(Gamelan(name = it.get(field = AppConstant.NAME)))
                }
                list
            } else {
                dummyGamelanList()
            }
        } catch (e: FirebaseException) {
            throw e
        }
    }
}