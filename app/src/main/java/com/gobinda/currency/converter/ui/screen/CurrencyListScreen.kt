package com.gobinda.currency.converter.ui.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.gobinda.currency.converter.ui.composable.CurrencyInfoView
import com.gobinda.currency.converter.ui.composable.MenuDivider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyListScreen(
    navController: NavController?,
    viewModel: CurrencyListViewModel = hiltViewModel()
) {

    val currencyInfoState = viewModel.currencyInfo.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                ),
                title = {
                    Text(text = "Currencies")
                },
                navigationIcon = {
                    IconButton(onClick = { navController?.navigateUp() }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            currencyInfoState.value?.let { currencyInfoItems ->
                val validList = currencyInfoItems.values.toList()
                LazyColumn {
                    items(
                        count = validList.size,
                        key = { i -> i }
                    ) {
                        CurrencyInfoView(currencyInfo = validList[it])
                        MenuDivider()
                    }
                }
            }
        }
    }
}