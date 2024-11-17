package com.example.labfinal.presentation.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.labfinal.di.ServiceLocator
import com.example.labfinal.domain.model.AssetDetail
import com.example.labfinal.domain.network.util.UiState
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssetDetailScreen(assetId: String, navController: NavHostController) {
    val repository = ServiceLocator.provideCryptoRepository(LocalContext.current)

    val viewModel: AssetDetailViewModel = viewModel(
        factory = AssetDetailViewModelFactory(repository, assetId)
    )

    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(text = if (uiState is UiState.Success) {
                        (uiState as UiState.Success<AssetDetail>).data.name
                    } else {
                        "Detalle del Asset"
                    })
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when (uiState) {
                is UiState.Loading -> {
                    CircularProgressIndicator()
                }

                is UiState.Success -> {
                    AssetDetailContent(assetDetail = (uiState as UiState.Success<AssetDetail>).data)
                }

                is UiState.Error -> {
                    val errorMessage = (uiState as UiState.Error).message
                    LaunchedEffect(errorMessage) {
                        snackbarHostState.showSnackbar(
                            message = errorMessage,
                            actionLabel = "Reintentar"
                        )
                        viewModel.retryFetchingDetails()
                    }
                    Text(
                        text = errorMessage,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
fun AssetDetailContent(assetDetail: AssetDetail) {
    val iconUrl = "https://assets.coincap.io/assets/icons/${assetDetail.symbol.lowercase()}@2x.png"

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = iconUrl),
            contentDescription = "${assetDetail.name} icon",
            modifier = Modifier.size(64.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = assetDetail.name,
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Symbol: ${assetDetail.symbol}",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Price: $${"%.2f".format(assetDetail.priceUsd)}",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Change (24h): ${"%.2f".format(assetDetail.changePercent24Hr)}%",
            color = if (assetDetail.changePercent24Hr >= 0) Color.Green else Color.Red,
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Supply: ${assetDetail.supply?.let { "%.2f".format(it) } ?: "N/A"}",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Max Supply: ${assetDetail.maxSupply?.let { "%.2f".format(it) } ?: "N/A"}",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Market Cap: $${assetDetail.marketCapUsd?.let { "%.2f".format(it) } ?: "N/A"}",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Última actualización: ${
                if (assetDetail.lastUpdated > 0) {
                    java.text.SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss",
                        java.util.Locale.getDefault()
                    ).format(assetDetail.lastUpdated)
                } else {
                    "No disponible"
                }
            }",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
    }
}
