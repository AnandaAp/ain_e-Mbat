package com.ain.embat.base

import android.app.Application
import android.content.Context
import com.ain.embat.di.nativeAndroidKoinModule
import com.google.android.gms.tflite.client.TfLiteInitializationOptions
import com.google.android.gms.tflite.internal.TfLiteJavaInitializerBase
import com.google.android.gms.tflite.java.TfLite
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
import org.tensorflow.lite.TensorFlowLite
import org.tensorflow.lite.task.gms.audio.TfLiteAudio
import org.tensorflow.lite.task.gms.audio.TfLiteTaskAudioInitializer
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
        Timber.asTree().e("Ain Embat Started")
        initMobileKoin(
            androidKoinModule(),
            nativeAndroidKoinModule(appContext),
            appDeclaration = {
                androidLogger()
                androidContext(this@BaseApplication)
            }
        )
        val settings = firestoreSettings {
            // Use memory cache
            setLocalCacheSettings(memoryCacheSettings {})
            setLocalCacheSettings(persistentCacheSettings {})
        }
        Firebase.initialize(this)
        Firebase.firestore.android.firestoreSettings = settings
        TensorFlowLite.init()
        TfLiteJavaInitializerBase(appContext)
        val optionsBuilder = TfLiteInitializationOptions
            .builder()
            .setEnableGpuDelegateSupport(true)
            .build()
        TfLite.initialize(appContext, optionsBuilder).addOnSuccessListener {
            Timber.tag("TensorFlow").e("TFLite successfully initialized: Using GPU")
            TfLiteTaskAudioInitializer(appContext)
            TfLiteAudio.initialize(appContext, optionsBuilder)
        }.addOnFailureListener {
            TfLite.initialize(appContext).addOnSuccessListener {
                Timber.tag("TensorFlow").e("TFLite successfully initialized: without GPU")
                TfLiteTaskAudioInitializer(appContext)
                TfLiteAudio.initialize(appContext, optionsBuilder)
            }.addOnFailureListener {
                Timber.tag("TensorFlow").e("TfLiteVision failed to initialize: $it")
            }
        }
    }
}