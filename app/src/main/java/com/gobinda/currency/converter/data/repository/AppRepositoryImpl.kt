package com.gobinda.currency.converter.data.repository

import android.content.Context
import com.gobinda.currency.converter.R
import com.gobinda.currency.converter.common.AppRepository
import com.gobinda.currency.converter.data.source.OpenExchangeApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(
    private val context: Context,
    private val openExchangeApi: OpenExchangeApi
) : AppRepository {

    private val _currencies = MutableStateFlow<Map<String, String>?>(null)
    override val currencies: StateFlow<Map<String, String>?> = _currencies

    private val _exchangeRate = MutableStateFlow<Map<String, Double>?>(null)
    override val exchangeRate: StateFlow<Map<String, Double>?> = _exchangeRate

    private val _requestStatus = MutableStateFlow<RequestStatus>(RequestStatus.NotStarted)
    override val requestStatus: StateFlow<RequestStatus> = _requestStatus.asStateFlow()

    override suspend fun requestForLatestData() {
        withContext(Dispatchers.IO) {
            _requestStatus.tryEmit(RequestStatus.Started)

            val appIdForServer = context.getString(R.string.APP_ID_OPEN_EXCHANGE_SERVER)
            val queryMap = mapOf("app_id" to appIdForServer)

            val currencyDef = async { openExchangeApi.getCurrencies(queryMap) }
            val exchangeRateDef = async { openExchangeApi.getExchangeRate(queryMap) }

            val currenciesResult = currencyDef.await()
            val exchangeRateResult = exchangeRateDef.await()

            var successfulQueryCounter = 0

            if (currenciesResult.isSuccessful) {
                currenciesResult.body()?.let {
                    _currencies.tryEmit(it)
                    successfulQueryCounter++
                }
            }

            if (exchangeRateResult.isSuccessful) {
                exchangeRateResult.body()?.let {
                    _exchangeRate.tryEmit(it.rates)
                    println(it.rates)
                    successfulQueryCounter++
                }
            }

            when (successfulQueryCounter == 2) {
                true -> _requestStatus.tryEmit(RequestStatus.Successful)
                else -> _requestStatus.tryEmit(RequestStatus.Failed)
            }
        }
    }

}