package viewmodel

import constants.AppConstant
import constants.AppConstant.BOTTOM_NAV_LIST
import constants.BottomNavigation
import constants.ExceptionConstant
import constants.ProductConstant
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.FirebaseException
import dev.gitlive.firebase.firestore.firestore
import getPlatformName
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import models.Product
import util.isNotNullOrEmpty

class ProductViewModel : BaseViewModel() {
    private val productMutableStateFlow = MutableStateFlow(Product())
    private val bottomNavItemsStateFlow = MutableStateFlow(mutableListOf<String>())
    val product = productMutableStateFlow.asStateFlow()
    val bottomNavItems = bottomNavItemsStateFlow.asStateFlow()

    private suspend fun getProducts(): Product {
        try {
            val productResponse = firestore
                .collection(AppConstant.PRODUCT)
                .get()
            return if (AppConstant.ANDROID.equals(getPlatformName(), ignoreCase = true) && productResponse.isNotNullOrEmpty()) {
                Product(
                    type = productResponse.documents.first().get(ProductConstant.TYPE),
                    status = productResponse.documents.first().get(ProductConstant.STATUS),
                    version = productResponse.documents.first().get(ProductConstant.VERSION)
                )
            } else {
                Product(
                    type = ExceptionConstant.NOT_ELIGIBLE
                )
            }
        } catch (e: FirebaseException) {
            throw e
        }
    }

    private suspend fun getBottomNavList(): List<String> {
        try {
            val listResponse = Firebase.firestore
                .collection(AppConstant.SHADY)
                .get()
            return if (listResponse.isNotNullOrEmpty()) {
                listResponse
                    .documents
                    .first()
                    .get(BOTTOM_NAV_LIST) as List<String>
            } else {
                listOf(
                    BottomNavigation.ngelaras,
                    BottomNavigation.rekaman
                )
            }
        } catch (e: FirebaseException) {
            throw e
        }
    }

    override suspend fun fetch() {
        try {
            fetchBottomNavigationItems()
            fetchProduct()
        } catch (e: FirebaseException) {
            throw e
        }
    }

    private suspend fun fetchBottomNavigationItems() {
        coroutine1.launch {
            val buttonNavItems = this.async {
                return@async getBottomNavList()
            }.await()
            if (buttonNavItems.isNotEmpty()) {
                buttonNavItems.forEach {
                    bottomNavItemsStateFlow.value.add(it)
                }
            }
        }
    }

    private suspend fun fetchProduct() {
        coroutine2.launch {
            val product = this.async {
                return@async getProducts()
            }.await()
            productMutableStateFlow.update {
                productMutableStateFlow.value.copy(
                    status = product.status,
                    type = product.type,
                    version = product.version
                )
            }
        }
    }
}