package com.example.labfinal.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.labfinal.data.repository.CryptoRepository
import com.example.labfinal.domain.model.AssetDetail
import com.example.labfinal.domain.network.util.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AssetDetailViewModel(
    private val repository: CryptoRepository,
    private val assetId: String
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<AssetDetail>>(UiState.Loading)
    val uiState: StateFlow<UiState<AssetDetail>> = _uiState

    init {
        fetchAssetDetails()
    }

    fun fetchAssetDetails() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val asset = repository.getAssetDetailsOnline(assetId)
                _uiState.value = UiState.Success(asset)
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Error cargando datos. Mostrando datos offline si están disponibles.")
                val offlineAsset = repository.getAssetDetailsOffline(assetId)
                if (offlineAsset != null) {
                    _uiState.value = UiState.Success(offlineAsset)
                } else {
                    _uiState.value = UiState.Error("No se encontraron datos offline para este asset.")
                }
            }
        }
    }

    fun retryFetchingDetails() {
        fetchAssetDetails()
    }
}
