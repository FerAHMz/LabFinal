package com.example.prueba.presentation.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.rememberAsyncImagePainter
import com.example.prueba.domain.model.Asset
import com.example.prueba.di.ServiceLocator
import kotlinx.coroutines.launch

@Composable
fun AssetsScreen(
    onAssetClick: (String) -> Unit,
    viewModel: AssetsViewModel = viewModel(factory = AssetsViewModelFactory(ServiceLocator.provideCryptoRepository(LocalContext.current)))
) {
    val assets by viewModel.assets.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val lastUpdated by viewModel.lastUpdated.collectAsState()

    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {
        Button(
            onClick = {
                coroutineScope.launch {
                    viewModel.saveDataForOffline()
                }
            },
            modifier = Modifier
                .align(Alignment.End)
                .padding(16.dp)
        ) {
            Text("Guardar Offline")
        }

        lastUpdated?.let {
            Text(
                text = "Última actualización: $it",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
            )
        }

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }
        errorMessage?.let {
            Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(16.dp))
        }

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(assets) { asset ->
                AssetItem(asset = asset, onClick = { onAssetClick(asset.id) })  // Navegamos usando el ID del activo
            }
        }
    }
}

@Composable
fun AssetItem(
    asset: Asset,
    onClick: () -> Unit
) {
    val iconUrl = "https://assets.coincap.io/assets/icons/${asset.symbol.lowercase()}@2x.png"

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = iconUrl),
            contentDescription = "${asset.symbol} icon",
            modifier = Modifier
                .size(40.dp)
                .padding(end = 8.dp)
        )

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = asset.name,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = asset.symbol,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }

        Text(
            text = "$${asset.priceUsd}",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(end = 8.dp)
        )

        val changeColor = if (asset.changePercent24Hr >= 0) Color.Green else Color.Red
        Text(
            text = "${asset.changePercent24Hr}%",
            color = changeColor,
            style = MaterialTheme.typography.bodySmall
        )
    }
}
