package plm.patientslikeme2.data.di

import android.app.Application
import android.content.Context
import com.amplitude.core.Amplitude
import com.amplitude.core.Configuration
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.HiltAndroidApp
import plm.patientslikeme2.BuildConfig

@HiltAndroidApp
class MyApplication : Application() {

    private var mFirebaseAnalytics: FirebaseAnalytics? = null

    override fun onCreate() {
        super.onCreate()
        instance = this
        appContext = applicationContext
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(appContext)

        registerAmplitude()
    }

    private fun registerAmplitude() {
        amplitude = Amplitude(Configuration(apiKey = BuildConfig.AMPLITUDE_API_KEY))
    }

    companion object {
        var unreadMessageCount: Int = 0
        var unreadNotificationCount: Int = 0
        lateinit var appContext: Context
        lateinit var instance: MyApplication
            private set

        //Based on environment api fetch the data
        val APP_MODE = Environment.LIVE
        var amplitude: Amplitude? = null
    }
}
