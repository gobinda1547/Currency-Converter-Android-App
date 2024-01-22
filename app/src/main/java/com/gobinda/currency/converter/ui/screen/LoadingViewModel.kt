package com.gobinda.currency.converter.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gobinda.currency.converter.common.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoadingViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {

    val loadingStatus = repository.requestStatus

    init {
        viewModelScope.launch {
            repository.requestForLatestData()
        }
    }
}