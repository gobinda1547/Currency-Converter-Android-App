package com.gobinda.currency.converter.common

import com.gobinda.currency.converter.data.model.ConverterOutput
import com.gobinda.currency.converter.data.model.CurrencyInfo
import com.gobinda.currency.converter.data.repository.RequestStatus
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext

interface AppRepository {

    val currencyInfo: StateFlow<Map<String, CurrencyInfo>?>

    val requestStatus: StateFlow<RequestStatus>

    suspend fun requestForLatestData()

    suspend fun calculateOutput(inputCountry: String, inputAmount: Double): List<ConverterOutput>

    suspend fun <T> asyncSafeCall(executeMe: suspend () -> T): Deferred<T?> {
        return withContext(Dispatchers.IO) {
            async {
                try {
                    executeMe()
                } catch (e: Exception) {
                    null
                }
            }
        }
    }
}