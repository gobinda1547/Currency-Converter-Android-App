package com.gobinda.currency.converter.data.repository

sealed class RequestStatus {
    data object Started:RequestStatus()
    data object NotStarted:RequestStatus()
    data object Successful:RequestStatus()
    data object Failed:RequestStatus()
}