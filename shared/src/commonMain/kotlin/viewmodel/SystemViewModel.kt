package viewmodel

import constants.AppConstant.SHADY_SUBSYSTEM
import constants.CollectionsConstant
import constants.FlagsConstant
import constants.isNotNullOrEmpty
import dev.gitlive.firebase.firestore.DocumentSnapshot
import dev.gitlive.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SystemViewModel: BaseViewModel() {
    private val systemOnboardingStateFlow = MutableStateFlow(mapOf<String, Any>())
    val systemOnboarding = systemOnboardingStateFlow.asStateFlow()
    override suspend fun fetch() {
        coroutine1.launch {
            try {
                val response = this.async {
                    val response: Map<String, Any> = getOnboardingSystem()
                    if(response.isNotEmpty()) {
                        systemOnboardingStateFlow.update { response }
                        runtimeCache.put(SHADY_SUBSYSTEM, response)
                    }
                }
                response.await()
            } catch (e: FirebaseFirestoreException) {
                throw e
            }
        }
    }

    private suspend fun <A> getOnboardingSystem(): Map<String, A> {
        try {
            val response = firestore
                .collectionGroup(CollectionsConstant.RETRIEVE_ONBOARDING_SYSTEM)
                .get()
            return if (response.isNotNullOrEmpty() && response.documents.first().isRetrieveSystemValid(
                    FlagsConstant.IS_ONBOARDING_FULL,
                    FlagsConstant.IS_SYSTEM_ON_TESTING
            )) {
                val map = mapOf(
                    FlagsConstant.IS_ONBOARDING_FULL to response.documents.first().get(FlagsConstant.IS_ONBOARDING_FULL) as Boolean,
                    FlagsConstant.IS_SYSTEM_ON_TESTING to response.documents.first().get(FlagsConstant.IS_SYSTEM_ON_TESTING) as Boolean
                )
                map as Map<String, A>
            } else {
                mapOf()
            }
        } catch (e: FirebaseFirestoreException) {
            throw e
        }
    }

    private fun DocumentSnapshot.isRetrieveSystemValid(vararg flags: String): Boolean {
        var output = true
        for (flag in flags) {
            if (this.contains(flag)) {
                output = output && this.contains(flag)
            }
        }
        return output
    }
}