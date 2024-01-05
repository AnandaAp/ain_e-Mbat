package com.ain.embat


import MainView
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ain.embat.base.BaseActivity
import com.ain.embat.ui.theme.Material3AinEmbatTheme
import constants.AppConstant
import constants.AppConstant.SHADY
import constants.Characters.EMPTY
import constants.ExceptionConstant
import constants.FlagsConstant
import constants.RuntimeCacheConstant.APP_PRODUCT
import util.isNotNullOrEmpty
import models.Product
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import ui.splash.SplashScreen
import viewmodel.NgelarasViewModel
import viewmodel.ProductViewModel
import viewmodel.SystemViewModel

class MainActivity : BaseActivity() {
    private val productViewModel: ProductViewModel by viewModel()
    private val systemViewModel: SystemViewModel  by viewModel()
    private val ngelarasViewModel: NgelarasViewModel by viewModel()
    private lateinit var _product: Product
    private lateinit var _bottomNavItems: List<String>
    private lateinit var eligibility: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Material3AinEmbatTheme {
                InitializeProduct()
            }
        }
    }

    /**
     * Initializes the first time launch of the app.
     */
    @Composable
    private fun InitializeProduct() {
        val system by systemViewModel.systemOnboarding.collectAsStateWithLifecycle()

        SplashScreen()
        LaunchedEffect(key1 = this, key2 = system) {
            productViewModel.fetch()
            systemViewModel.fetch()
            ngelarasViewModel.fetch()

            val map: Any = runtimeCache.get(AppConstant.SHADY_SUBSYSTEM) ?: mapOf<String, Any>()
            var isOnboardingFull = EMPTY
            if ((map as Map<*, *>).isNotEmpty()) {
                isOnboardingFull = map[FlagsConstant.IS_ONBOARDING_FULL].toString()
            }
            if (isOnboardingFull.isNotNullOrEmpty()) {
                Timber.tag(TAG).e("Shady Subsystem:[\n\t$map\n]")
            }
        }
        HandleAppBasedByProduct()
    }

    /**
     * Handle the app operation based from product type which retrieved from Firestore.
     */
    @Composable
    fun HandleAppBasedByProduct() {
        val product by productViewModel.product.collectAsStateWithLifecycle()
        val bottomNavItems by productViewModel.bottomNavItems.collectAsStateWithLifecycle()
        _product = product
        _bottomNavItems = bottomNavItems
        LaunchedEffect(key1 = product, key2 = bottomNavItems) {
            eligibility = eligibilityProvider(product)
            if (ExceptionConstant.NOT_ELIGIBLE != eligibility) {
                runtimeCache.put(APP_PRODUCT, product)
                Timber.tag(TAG).d(runtimeCache.getString(APP_PRODUCT))
            }
            if (bottomNavItemsProvider(bottomNavItems).isNotEmpty()) {
                runtimeCache.put(SHADY, bottomNavItemsProvider(bottomNavItems))
                Timber.tag(TAG).d(runtimeCache.getString(SHADY))
            }
        }
        RenderView()
    }

    @Composable
    private fun RenderView() {
        Crossfade(
            targetState = _product.type,
            label = TAG!!
        ) { productType ->
            when (productType) {
                AppConstant.Type.ANDROID_ONLY -> MainView(runtimeCache, ngelarasViewModel)
                AppConstant.Type.FULL_TYPE -> MainView(runtimeCache, ngelarasViewModel)
                AppConstant.Type.IOS_ONLY -> SplashScreen()
                else -> SplashScreen()
            }
        }
    }

    /**
     * Return the string value of product type.
     * @return String
     */
    private fun eligibilityProvider(product: Product): String =
        if (AppConstant.PROTOTYPE == product.status) {
            when(product.type) {
                AppConstant.Type.ANDROID_ONLY -> ExceptionConstant.ONLY_ELIGIBLE_FOR_ANDROID
                AppConstant.Type.IOS_ONLY -> ExceptionConstant.ONLY_ELIGIBLE_FOR_IOS
                AppConstant.Type.FULL_TYPE -> ExceptionConstant.FULL_ELIGIBLE
                else -> ExceptionConstant.NOT_ELIGIBLE
            }
        } else {
            ExceptionConstant.NOT_ELIGIBLE
        }

    private fun bottomNavItemsProvider(bottomNavItems: List<String>) =
        bottomNavItems.ifEmpty {
            listOf()
        }
}

@Preview
@Composable
fun AinPreview() {
    Material3AinEmbatTheme {
        MainView()
    }
}