package plm.patientslikeme2.data.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import plm.patientslikeme2.BuildConfig
import plm.patientslikeme2.data.api.ApiServices
import plm.patientslikeme2.data.api.network.LiveDataCallAdapterFactoryForRetrofit
import plm.patientslikeme2.data.db.AppDatabase
import plm.patientslikeme2.data.db.UserDao
import plm.patientslikeme2.utils.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    /**
     * Provides ApiServices client for Retrofit
     */
    @Singleton
    @Provides
    fun provideService(): ApiServices {
        val authenticator = TokenAuthenticator()
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(getRetrofitClient(authenticator))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactoryForRetrofit())
            .build()
            .create(ApiServices::class.java)
    }

    @Singleton
    @Provides
    fun providesOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(authInterceptor).build()
    }

    private fun getRetrofitClient(authenticator: Authenticator? = null): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                chain.proceed(chain.request().newBuilder().also {
                    it.addHeader(Constants.APP_TYPE, Constants.ANDROID)
                }.build())
            }.also { client ->
                authenticator?.let { client.authenticator(it) }
                if (BuildConfig.DEBUG) {
                    val logging = HttpLoggingInterceptor()
                    logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                    client.addInterceptor(logging)
                }
            }.also { client ->
                client.connectTimeout(30,TimeUnit.SECONDS)
                client.readTimeout(30,TimeUnit.SECONDS)
                client.writeTimeout(30,TimeUnit.SECONDS)}.build()
    }

    /**
     * Provides app AppDatabase
     */
    @Singleton
    @Provides
    fun provideDb(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "patientlikeme-db")
            .fallbackToDestructiveMigration().build()

    /**
     * Provides UserDao an object to access user table from Database
     */
    @Singleton
    @Provides
    fun provideUserDao(db: AppDatabase): UserDao = db.userModel()
}
