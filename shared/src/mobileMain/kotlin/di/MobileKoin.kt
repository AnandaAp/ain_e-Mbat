package di
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import viewmodel.BaseViewModel
import viewmodel.NgelarasViewModel
import viewmodel.ProductViewModel
import viewmodel.SystemViewModel
fun mobileModule() = module {
    includes(appModule())
    single { Firebase.firestore }
    factory { SystemViewModel() }
    factory { ProductViewModel() }
    factory { BaseViewModel() }
    factory { NgelarasViewModel() }
}

fun initMobileKoin(
    appDeclaration: KoinAppDeclaration = { }
) = startKoin {
    appDeclaration()
    modules(mobileModule())
}

fun initMobileKoin() = initMobileKoin { }