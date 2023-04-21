package com.example.quiztree.di

import com.example.quiztree.AppConstants
import com.example.quiztree.data.remote.QuizApiServices
import com.example.quiztree.utils.TLSSocketFactory
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    fun provideQuizApiServices(okHttpClient: OkHttpClient): QuizApiServices {
        val retrofit = Retrofit.Builder()
            .baseUrl(AppConstants.Api.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().serializeNulls().create()
                )
            )
            .build()
        return retrofit.create(QuizApiServices::class.java)
    }

    @Provides
    fun provideOkHttpClient(
    ): OkHttpClient {
        val tlsSocketFactory = TLSSocketFactory()
        val okHttpClientBuilder = OkHttpClient().newBuilder()
            .sslSocketFactory(tlsSocketFactory, tlsSocketFactory.trustManager)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS))
        return okHttpClientBuilder.build()
    }
}