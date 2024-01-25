package com.gobinda.currency.converter.ui.screen

import androidx.lifecycle.ViewModel
import com.gobinda.currency.converter.common.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CurrencyListViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {
    val currencyInfo = repository.currencyInfo
}