package di
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import viewmodel.basic.BaseViewModel
import viewmodel.basic.NgelarasViewModel
import viewmodel.basic.ProductViewModel
import viewmodel.basic.SystemViewModel
fun mobileModule() = module {
    includes(appModule())
    single { Firebase.firestore }
    factory { SystemViewModel() }
    factory { ProductViewModel() }
    factory { BaseViewModel() }
    factory { NgelarasViewModel() }
}

fun initMobileKoin(
    additionalModules: Module = Module(),
    appDeclaration: KoinAppDeclaration = { }
): KoinApplication {
    return startKoin {
        appDeclaration()
        modules(mobileModule(), additionalModules)
    }
}

fun initMobileKoin() = initMobileKoin { }