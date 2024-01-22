package com.gobinda.currency.converter.ui.screen

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.gobinda.currency.converter.R
import com.gobinda.currency.converter.data.repository.RequestStatus

@Composable
fun LoadingScreen(
    navController: NavController?,
    viewModel: LoadingViewModel = hiltViewModel()
) {

    val requestStatus = viewModel.loadingStatus.collectAsState()

    Box(modifier = Modifier.wrapContentSize()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            PhotographicIndication(requestStatus = requestStatus.value)
            Spacer(modifier = Modifier.height(30.dp))
            TextIndication(requestStatus = requestStatus.value)
        }
    }
}

@Composable
private fun PhotographicIndication(requestStatus: RequestStatus) {
    val rotation = remember { Animatable(0f) }

    LaunchedEffect(key1 = rotation) {
        rotation.animateTo(
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(3000)
            )
        )
    }

    val backgroundColor = when (requestStatus) {
        RequestStatus.Successful -> Color.Green
        RequestStatus.Failed -> Color.Red
        RequestStatus.Started, RequestStatus.NotStarted -> Color.LightGray
    }

    Box(
        modifier = Modifier
            .size(size = 50.dp)
            .graphicsLayer { rotationZ = rotation.value }
            .background(color = backgroundColor)
    )
}

@Composable
private fun TextIndication(requestStatus: RequestStatus) {
    when (requestStatus) {
        RequestStatus.Successful -> R.string.text_exchange_rate_fetch_success
        RequestStatus.Failed -> R.string.text_exchange_rate_fetch_problem
        RequestStatus.NotStarted, RequestStatus.Started -> R.string.text_fetching_exchange_rate
    }.let { Text(text = stringResource(id = it)) }
}