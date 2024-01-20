package com.gobinda.currency.converter.common

import com.gobinda.currency.converter.data.repository.RequestStatus
import kotlinx.coroutines.flow.StateFlow

interface AppRepository {

    val currencies: StateFlow<Map<String, String>?>

    val exchangeRate: StateFlow<Map<String, Double>?>

    val requestStatus: StateFlow<RequestStatus>

    suspend fun requestForLatestData()
}