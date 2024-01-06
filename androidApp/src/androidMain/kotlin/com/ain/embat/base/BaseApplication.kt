package com.ain.embat.base

import android.app.Application
import android.content.Context
import com.google.firebase.firestore.LocalCacheSettings
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.firestore.ktx.memoryCacheSettings
import com.google.firebase.firestore.ktx.persistentCacheSettings
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import dev.gitlive.firebase.initialize
import di.androidKoinModule
import di.initMobileKoin
import org.conscrypt.Conscrypt
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.component.KoinComponent
import timber.log.Timber
import timber.log.Timber.DebugTree
import java.security.Security


class BaseApplication: Application(), KoinComponent, LocalCacheSettings {
    private lateinit var appContext: Context

    @Deprecated("#firestoreSettings function is deprecated")
    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        Security.insertProviderAt(Conscrypt.newProvider(), 1)
        Timber.plant(DebugTree())
        initMobileKoin(appDeclaration = {
            androidLogger()
            androidContext(this@BaseApplication)
        }, additionalModules = androidKoinModule())
        val settings = firestoreSettings {
            // Use memory cache
            setLocalCacheSettings(memoryCacheSettings {})
            setLocalCacheSettings(persistentCacheSettings {})
        }
        Firebase.initialize(this)
        Firebase.firestore.android.firestoreSettings = settings
    }
}