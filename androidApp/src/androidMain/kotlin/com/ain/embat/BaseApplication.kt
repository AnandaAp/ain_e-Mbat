package com.ain.embat

import android.app.Application
import android.content.Context
import com.google.firebase.firestore.FirebaseFirestoreSettings
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import dev.gitlive.firebase.initialize
import di.initKoin
import org.conscrypt.Conscrypt
import org.koin.core.component.KoinComponent
import timber.log.Timber
import timber.log.Timber.DebugTree
import java.security.Security


class BaseApplication: Application(), KoinComponent {
    private lateinit var appContext: Context

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        Security.insertProviderAt(Conscrypt.newProvider(), 1);
        Timber.plant(DebugTree())
        initKoin()
        Firebase.initialize(this)
        Firebase.firestore.android.firestoreSettings = FirebaseFirestoreSettings.Builder(Firebase.firestore.android.firestoreSettings)
            .setPersistenceEnabled(false)
            .build()
    }
}