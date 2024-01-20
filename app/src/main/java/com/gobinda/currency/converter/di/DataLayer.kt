package com.gobinda.currency.converter.di

import android.content.Context
import com.gobinda.currency.converter.common.AppRepository
import com.gobinda.currency.converter.data.repository.AppRepositoryImpl
import com.gobinda.currency.converter.data.source.OpenExchangeApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DataLayer {

    private const val OPEN_EXCHANGE_API_BASE_URL = "https://openexchangerates.org/"

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl(OPEN_EXCHANGE_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideOpenExchangeApi(retrofit: Retrofit): OpenExchangeApi {
        return retrofit.create(OpenExchangeApi::class.java)
    }

    @Singleton
    @Provides
    fun provideAppRepository(
        @ApplicationContext context: Context,
        openExchangeApi: OpenExchangeApi
    ): AppRepository {
        return AppRepositoryImpl(context, openExchangeApi)
    }
}