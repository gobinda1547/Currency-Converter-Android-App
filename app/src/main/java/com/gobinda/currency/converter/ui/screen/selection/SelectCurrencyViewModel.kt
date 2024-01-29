package com.gobinda.currency.converter.ui.screen.selection

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gobinda.currency.converter.common.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectCurrencyViewModel @Inject constructor(
    repository: AppRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val currencyInfo = repository.currencyInfo

    private val _inputCountry = MutableStateFlow(savedStateHandle["inputCountry"] ?: "BDT")
    val inputCountry: StateFlow<String> get() = _inputCountry

    fun handleEvent(event: SelectCurrencyEvent) = viewModelScope.launch {
        when (event) {
            is SelectCurrencyEvent.UpdateCurrency -> {
                _inputCountry.tryEmit(event.newCurrency)
            }
        }
    }
}