package com.example.labfinal.presentation.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import coil.compose.rememberAsyncImagePainter
import com.example.labfinal.domain.model.Asset
import com.example.labfinal.di.ServiceLocator
import com.example.labfinal.domain.network.util.UiState
import kotlinx.coroutines.launch

@Composable
fun AssetsScreen(
    onAssetClick: (String) -> Unit,
    viewModel: AssetsViewModel = viewModel(factory = AssetsViewModelFactory(ServiceLocator.provideCryptoRepository(LocalContext.current)))
) {
    val uiState by viewModel.uiState.collectAsState()
    val lastUpdated by viewModel.lastUpdated.collectAsState()

    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Button(
                onClick = {
                    coroutineScope.launch {
                        try {
                            viewModel.saveDataForOffline()
                            snackbarHostState.showSnackbar("Datos guardados offline.")
                        } catch (e: Exception) {
                            snackbarHostState.showSnackbar("Error al guardar datos offline.")
                        }
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

            when (uiState) {
                is UiState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.5f)),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is UiState.Success -> {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items((uiState as UiState.Success<List<Asset>>).data) { asset ->
                            AssetItem(asset = asset, onClick = { onAssetClick(asset.id) })
                        }
                    }
                }

                is UiState.Error -> {
                    val errorMessage = (uiState as UiState.Error).message
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(16.dp)
                    )
                }
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
            text = "$${"%.2f".format(asset.priceUsd)}",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(end = 8.dp)
        )

        val changeColor = if (asset.changePercent24Hr >= 0) Color.Green else Color.Red
        Text(
            text = "${"%.2f".format(asset.changePercent24Hr)}%",
            color = changeColor,
            style = MaterialTheme.typography.bodySmall
        )
    }
}
