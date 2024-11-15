package com.example.prueba.presentation.profile

import AssetDetailViewModelFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.rememberAsyncImagePainter
import com.example.prueba.di.ServiceLocator
import com.example.prueba.domain.model.AssetDetail

@Composable
fun AssetDetailScreen(
    assetId: String
) {
    val repository = ServiceLocator.provideCryptoRepository(LocalContext.current)

    val viewModel: AssetDetailViewModel = viewModel(
        factory = AssetDetailViewModelFactory(repository, assetId)
    )

    val assetDetail by viewModel.assetDetail.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isLoading) {
            CircularProgressIndicator()
        }
        errorMessage?.let {
            Text(text = it, color = MaterialTheme.colorScheme.error)
        }
        assetDetail?.let {
            AssetDetailContent(assetDetail = it)
        }
    }
}

@Composable
fun AssetDetailContent(assetDetail: AssetDetail) {
    val iconUrl = "https://assets.coincap.io/assets/icons/${assetDetail.symbol.lowercase()}@2x.png"
    Image(
        painter = rememberAsyncImagePainter(model = iconUrl),
        contentDescription = null,
        modifier = Modifier.size(64.dp)
    )

    Spacer(modifier = Modifier.height(8.dp))
    Text(text = assetDetail.name, style = MaterialTheme.typography.headlineLarge)
    Spacer(modifier = Modifier.height(8.dp))
    Text(text = "Symbol: ${assetDetail.symbol}")
    Text(text = "Price: ${assetDetail.priceUsd}")
    Text(text = "Change (24h): ${assetDetail.changePercent24Hr}%")
}
