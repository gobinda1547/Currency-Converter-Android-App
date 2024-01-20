package com.gobinda.currency.converter.data.source

import com.gobinda.currency.converter.data.model.ExchangeRateInfo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface OpenExchangeApi {
    @GET("/api/currencies.json")
    suspend fun getCurrencies(
        @QueryMap queryMap: Map<String, String>
    ): Response<Map<String, String>>

    @GET("/api/latest.json")
    suspend fun getExchangeRate(
        @QueryMap queryMap: Map<String, String>
    ): Response<ExchangeRateInfo>
}