package com.ain.embat.ui.main


import MainView
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ain.embat.base.BaseActivity
import com.ain.embat.ui.theme.Material3AinEmbatTheme
import com.ain.embat.utils.AndroidNavigator
import viewmodel.StarterViewModel
import constants.AppConstant
import constants.AppConstant.SHADY
import constants.Characters.EMPTY
import constants.ExceptionConstant
import constants.FlagsConstant
import constants.RuntimeCacheConstant.APP_PRODUCT
import models.Product
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import ui.splash.SplashScreen
import util.isNotNullOrEmpty
import viewmodel.basic.NgelarasViewModel
import viewmodel.basic.ProductViewModel
import viewmodel.basic.SystemViewModel

class MainActivity : BaseActivity() {
    private val productViewModel: ProductViewModel by viewModel()
    private val systemViewModel: SystemViewModel by viewModel()
    private val ngelarasViewModel: NgelarasViewModel by viewModel()
    private val starterViewModel: StarterViewModel by viewModel()
    private lateinit var _product: Product
    private lateinit var _bottomNavItems: List<String>
    private lateinit var eligibility: String

    @Composable
    override fun InitiateUI() {
        val permissionStatus by starterViewModel.permissionStatus.collectAsStateWithLifecycle()
        var permissionCollection by remember { mutableIntStateOf(-1) }
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions(),
            onResult = { result -> permissionCollection.plus(onResultCalled(result = result)) }
        )
        SideEffect {
            initializePermission(permissionStatus, launcher)
            if (permissionCollection <= 0 && !permissionStatus) {
                launcher.launch(starterViewModel.permissions)
            }
        }
        InitializeProduct()
    }

    private fun onResultCalled(result: Map<String, Boolean>): Int {
        var output = 0
        result.let {
            it["android.permission.RECORD_AUDIO"]?.let { isGranted ->
                Timber.tag("StarterViewModel").e("permission for record audio is $isGranted")
                if (isGranted) {
                    output++
                }
            }
            it["android.permission.READ_MEDIA_AUDIO"]?.let { isGranted ->
                Timber.tag("StarterViewModel").e("permission for read media audio is $isGranted")
                if (isGranted) {
                    output++
                }
            }
        }
        return output
    }

    private fun initializePermission(permissionStatus: Boolean, launcher: ManagedActivityResultLauncher<Array<String>, Map<String, @JvmSuppressWildcards Boolean>>) {
        starterViewModel.checkPermission { isGranted ->
            starterViewModel.updatePermissionStatus(isGranted)
            if (!permissionStatus) {
                launcher.launch(starterViewModel.permissions)
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

            val map: Any = cache.get(AppConstant.SHADY_SUBSYSTEM) ?: mapOf<String, Any>()
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
                cache.put(APP_PRODUCT, product)
                Timber.tag(TAG).d(cache.getString(APP_PRODUCT))
            }
            if (bottomNavItemsProvider(bottomNavItems).isNotEmpty()) {
                cache.put(SHADY, bottomNavItemsProvider(bottomNavItems))
                Timber.tag(TAG).d(cache.getString(SHADY))
            }
        }
        RenderView()
    }

    @Composable
    private fun RenderView() {
        val context = LocalContext.current
        Crossfade(
            targetState = _product.type,
            label = TAG!!
        ) { productType ->
            when (productType) {
                AppConstant.Type.ANDROID_ONLY, AppConstant.Type.FULL_TYPE -> MainView(
                    cache,
                    ngelarasViewModel,
                    AndroidNavigator(context = context)
                )
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
    val context = LocalContext.current
    Material3AinEmbatTheme {
        MainView(navigator = AndroidNavigator(context = context))
    }
}