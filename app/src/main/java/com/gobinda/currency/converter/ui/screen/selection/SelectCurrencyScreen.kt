package com.gobinda.currency.converter.ui.screen.selection

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.gobinda.currency.converter.ui.composable.CurrencyInfoView
import com.gobinda.currency.converter.ui.composable.MenuDivider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectCurrencyScreen(
    navController: NavController,
    viewModel: SelectCurrencyViewModel = hiltViewModel()
) {

    val currencyInfoState = viewModel.currencyInfo.collectAsState()
    val inputCountry = viewModel.inputCountry.collectAsState()

    fun handleDoneButtonPressed() {
        navController.apply {
            previousBackStackEntry?.savedStateHandle?.set("outputCountry", inputCountry.value)
            navigateUp()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    actionIconContentColor = MaterialTheme.colorScheme.onBackground
                ),
                title = {
                    val info = currencyInfoState.value?.get(inputCountry.value) ?: return@TopAppBar
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "Selected (")
                        Spacer(modifier = Modifier.size(8.dp))
                        Image(
                            modifier = Modifier.width(30.dp),
                            bitmap = info.image.asImageBitmap(),
                            contentDescription = "",
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(
                            text = inputCountry.value,
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 1
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(text = ")")
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { handleDoneButtonPressed() }) {
                        Icon(imageVector = Icons.Filled.Done, contentDescription = "Done")
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

            val currencyInfoItems = currencyInfoState.value ?: return@Surface
            val validList = currencyInfoItems.values.toList()

            fun handleOnItemClick(codeName: String) {
                viewModel.handleEvent(SelectCurrencyEvent.UpdateCurrency(codeName))
            }

            @Composable
            fun getBackgroundColor(currentCodeName: String): Color {
                return when (inputCountry.value == currentCodeName) {
                    true -> MaterialTheme.colorScheme.background
                    else -> MaterialTheme.colorScheme.surface
                }
            }

            LazyColumn {
                items(
                    count = validList.size,
                    key = { i -> i }
                ) { position ->
                    CurrencyInfoView(
                        currencyInfo = validList[position],
                        backgroundColor = getBackgroundColor(validList[position].codeName),
                        onItemClick = { handleOnItemClick(validList[position].codeName) }
                    )
                    MenuDivider()
                }
            }
        }
    }
}