package com.gobinda.currency.converter.data.repository

import android.content.Context
import com.gobinda.currency.converter.R
import com.gobinda.currency.converter.common.AppRepository
import com.gobinda.currency.converter.data.model.ConverterOutput
import com.gobinda.currency.converter.data.model.CurrencyInfo
import com.gobinda.currency.converter.data.source.OpenExchangeApi
import com.gobinda.currency.converter.imagekit.CountryImageProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

private const val UP_TO_DECIMAL_PLACE = 2
private val CALCULATION_ROUND_MODE = RoundingMode.HALF_UP

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

            val finalResult = currencyData.entries.mapNotNull { mapEntry ->
                val rateValue = exchangeRateData[mapEntry.key] ?: return@mapNotNull null
                CurrencyInfo(
                    codeName = mapEntry.key,
                    fullName = mapEntry.value,
                    rate = rateValue,
                    image = CountryImageProvider.getBitmap(context, mapEntry.key)
                )
            }.associateBy { it.codeName }

            _currencyInfo.emit(finalResult)
            _requestStatus.emit(RequestStatus.Successful)
        }
    }

    override suspend fun calculateOutput(
        inputCountry: String,
        inputAmount: Double
    ): List<ConverterOutput> {
        val validCurrencyInfo = currencyInfo.value ?: return emptyList()
        val firstDivisorInDecimal = validCurrencyInfo[inputCountry]?.let {
            BigDecimal(it.rate.toString())
        } ?: return emptyList()

        val rawAmountInDecimal = BigDecimal(inputAmount)
        val amountInDollar = rawAmountInDecimal.divide(
            firstDivisorInDecimal, UP_TO_DECIMAL_PLACE, CALCULATION_ROUND_MODE
        )

        return validCurrencyInfo.entries.map {
            val exchangeRateInDecimal = BigDecimal(it.value.rate.toString())
            val finalResult = amountInDollar.multiply(exchangeRateInDecimal)
            ConverterOutput(
                currencyInfo = it.value,
                outputAmount = finalResult.toPlainString()
            )
        }
    }
}