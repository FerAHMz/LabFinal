package com.example.prueba.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prueba.data.repository.CryptoRepository
import com.example.prueba.domain.model.AssetDetail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AssetDetailViewModel(
    private val repository: CryptoRepository,
    private val assetId: String
) : ViewModel() {

    private val _assetDetail = MutableStateFlow<AssetDetail?>(null)
    val assetDetail: StateFlow<AssetDetail?> = _assetDetail

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        fetchAssetDetails()
    }

    private fun fetchAssetDetails() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val details = repository.getAssetDetailsOnline(assetId)
                _assetDetail.value = details
            } catch (e: Exception) {
                _errorMessage.value = "Error loading asset details. Showing offline data."
                _assetDetail.value = repository.getAssetDetailsOffline(assetId)
            } finally {
                _isLoading.value = false
            }
        }
    }
}
