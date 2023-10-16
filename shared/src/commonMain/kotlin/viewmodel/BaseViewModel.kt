package viewmodel

import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import di.RuntimeCache
import kotlinx.coroutines.CoroutineScope
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import viewmodel.interfaces.Behaviours

open class BaseViewModel: ViewModel(), KoinComponent, Behaviours {
    protected val runtimeCache: RuntimeCache by inject()
    protected val coroutine1: CoroutineScope by inject()
    protected val coroutine2: CoroutineScope by inject()
    protected val firestore: FirebaseFirestore by inject()
    override suspend fun fetch() {
        //Do nothing
    }
}