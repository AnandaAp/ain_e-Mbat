package di

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.plus
import org.koin.dsl.module
import viewmodel.BaseViewModel
import viewmodel.ProductViewModel
import viewmodel.SystemViewModel

fun appModule() = module {
    single { Firebase.firestore }
    single { RuntimeCache() }
    factory {
        val handler = CoroutineExceptionHandler { _, exception ->
            println("CoroutineExceptionHandler got $exception")
        }
        CoroutineScope(Dispatchers.IO) + handler
    }
    factory { SystemViewModel() }
    factory { ProductViewModel() }
    factory { BaseViewModel() }
}