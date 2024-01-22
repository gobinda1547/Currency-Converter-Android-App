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

            var successfulResponseCounter = 0
            val appIdForServer = context.getString(R.string.APP_ID_OPEN_EXCHANGE_SERVER)
            val queryMap = mapOf("app_id" to appIdForServer)

            val currencyDeferred = async {
                try {
                    val currencyResponse = openExchangeApi.getCurrencies(queryMap)
                    if (currencyResponse.isSuccessful) {
                        currencyResponse.body()?.let { currencyValue ->
                            _currencies.tryEmit(currencyValue)
                            successfulResponseCounter++
                        }
                    }
                } catch (ignore: Exception) {
                }
            }
            val exchangeRateDeferred = async {
                try {
                    val exchangeRateResponse = openExchangeApi.getExchangeRate(queryMap)
                    if (exchangeRateResponse.isSuccessful) {
                        exchangeRateResponse.body()?.let { exchangeRateValue ->
                            _exchangeRate.tryEmit(exchangeRateValue.rates)
                            successfulResponseCounter++
                        }
                    }
                } catch (ignore: Exception) {
                }
            }

            currencyDeferred.await()
            exchangeRateDeferred.await()

            _requestStatus.emit(
                when (successfulResponseCounter == 2) {
                    true -> RequestStatus.Successful
                    else -> RequestStatus.Failed
                }
            )
        }
    }
}