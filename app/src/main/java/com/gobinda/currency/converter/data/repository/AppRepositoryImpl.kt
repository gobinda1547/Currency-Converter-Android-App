package com.gobinda.currency.converter.data.repository

import android.content.Context
import android.util.Log
import com.gobinda.currency.converter.R
import com.gobinda.currency.converter.common.AppRepository
import com.gobinda.currency.converter.data.model.CurrencyInfo
import com.gobinda.currency.converter.data.source.OpenExchangeApi
import com.gobindacurrency.converter.countryimages.CountryImageProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val TAG = "AppRepositoryImpl"

class AppRepositoryImpl @Inject constructor(
    private val context: Context,
    private val openExchangeApi: OpenExchangeApi
) : AppRepository {

    private val _currencyInfo = MutableStateFlow<Map<String, CurrencyInfo>?>(null)
    override val currencyInfo: StateFlow<Map<String, CurrencyInfo>?> = _currencyInfo

    private val _requestStatus = MutableStateFlow<RequestStatus>(RequestStatus.NotStarted)
    override val requestStatus: StateFlow<RequestStatus> = _requestStatus.asStateFlow()

    override suspend fun requestForLatestData() {
        withContext(Dispatchers.IO) {
            _requestStatus.tryEmit(RequestStatus.Started)

            val appIdForServer = context.getString(R.string.APP_ID_OPEN_EXCHANGE_SERVER)
            val queryMap = mapOf("app_id" to appIdForServer)

            val currencyDef = asyncSafeCall { openExchangeApi.getCurrencies(queryMap) }
            val exchangeRateDef = asyncSafeCall { openExchangeApi.getExchangeRate(queryMap) }

            val currencyResponse = currencyDef.await()
            val exchangeRateResponse = exchangeRateDef.await()

            val currencyData = currencyResponse?.let {
                when (it.isSuccessful) {
                    true -> it.body()
                    else -> null
                }
            }
            val exchangeRateData = exchangeRateResponse?.let {
                when (it.isSuccessful) {
                    true -> it.body()?.rates
                    else -> null
                }
            }

            if (currencyData == null || exchangeRateData == null) {
                _requestStatus.emit(RequestStatus.Failed)
                return@withContext
            }

            //Log.i(TAG, "requestForLatestData: total currency = ${currencyData.size}")
            //Log.i(TAG, "requestForLatestData: total rate = ${exchangeRateData.size}")

            val finalResult = currencyData.entries.mapNotNull { mapEntry ->
                val rateValue = exchangeRateData[mapEntry.key] ?: return@mapNotNull null
                CurrencyInfo(
                    codeName = mapEntry.key,
                    fullName = mapEntry.value,
                    rate = rateValue,
                    imageName = CountryImageProvider.getFileNameFromCountryCode(mapEntry.key),
                    isImageAvailable = CountryImageProvider.doesExist(context, mapEntry.key)
                )
            }.associateBy { it.codeName }
            //Log.i(TAG, "requestForLatestData: final result size = ${finalResult.size}")

            _currencyInfo.emit(finalResult)
            _requestStatus.emit(RequestStatus.Successful)
        }
    }
}