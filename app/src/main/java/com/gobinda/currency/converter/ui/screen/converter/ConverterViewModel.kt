package com.gobinda.currency.converter.ui.screen.converter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gobinda.currency.converter.common.AppRepository
import com.gobinda.currency.converter.data.model.ConverterOutput
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class ConverterViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {

    private val _inputAmount = MutableStateFlow("")
    val inputAmount: StateFlow<String> get() = _inputAmount

    private val _inputCountry = MutableStateFlow("BDT")
    val inputCountry: StateFlow<String> get() = _inputCountry

    private val _outputList = MutableStateFlow<List<ConverterOutput>>(emptyList())
    val outputList: StateFlow<List<ConverterOutput>> get() = _outputList

    val currencyInfo = repository.currencyInfo

    private val debounceCalculationReq = MutableSharedFlow<Pair<String, String>>()

    init {
        viewModelScope.launch {
            debounceCalculationReq.debounce(500L).collect {
                recalculateResult(it.first, it.second)
            }
        }
    }

    fun handleEvent(event: ConverterEvent) = viewModelScope.launch {
        when (event) {
            is ConverterEvent.UpdateAmount -> {
                if (Regex("^[0-9]*(\\.[0-9]*)?$").matches(event.amount)) {
                    _inputAmount.tryEmit(event.amount)
                }
            }

            is ConverterEvent.UpdateCountry -> {
                currencyInfo.value?.get(event.countryCode)?.let {
                    _inputCountry.tryEmit(event.countryCode)
                }
            }
        }

        val newCalculationEvent = Pair(inputCountry.value, inputAmount.value)
        debounceCalculationReq.emit(newCalculationEvent)
    }

    private suspend fun recalculateResult(inputCountry: String, inputAmount: String) {
        val amount: Double = inputAmount.toDoubleOrNull() ?: let {
            _outputList.tryEmit(emptyList())
            return
        }
        val newResult = repository.calculateOutput(inputCountry, amount)
        _outputList.tryEmit(newResult)
    }
}