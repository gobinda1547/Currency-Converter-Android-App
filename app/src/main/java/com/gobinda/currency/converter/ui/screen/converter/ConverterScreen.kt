package com.gobinda.currency.converter.ui.screen.converter

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.gobinda.currency.converter.ui.composable.ConverterInputView
import com.gobinda.currency.converter.ui.composable.ConverterOutputView
import com.gobinda.currency.converter.ui.composable.MenuDivider
import com.gobinda.currency.converter.ui.navigation.AppScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConverterScreen(
    navController: NavController,
    viewModel: ConverterViewModel = hiltViewModel()
) {

    val outputList = viewModel.outputList.collectAsState()
    val currencyInfo = viewModel.currencyInfo.collectAsState()
    val inputAmount = viewModel.inputAmount.collectAsState()
    val inputCountryCode = viewModel.inputCountry.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                ),
                title = {
                    Text(text = "Currency converter")
                },
                actions = {
                    IconButton(onClick = { openCurrencyListScreen(navController) }) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = "Currency info",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
            )
        }
    ) { innerPadding ->

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column {
                MenuDivider(color = MaterialTheme.colorScheme.surface)
                currencyInfo.value?.get(inputCountryCode.value)?.let { currencyInfo ->
                    ConverterInputView(
                        image = currencyInfo.image,
                        codeName = inputCountryCode.value,
                        amount = inputAmount.value,
                        onAmountChanged = {
                            viewModel.handleEvent(ConverterEvent.UpdateAmount(amount = it))
                        },
                        onCountryChangeClicked = {

                        }
                    )
                }
                if (outputList.value.isNotEmpty()) {
                    ConverterOutputView(outputList = outputList.value)
                }
            }
        }
    }

}

private fun openCurrencyListScreen(navController: NavController) {
    navController.navigate(AppScreen.CurrencyListScreen.route)
}