package com.example.prueba.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prueba.data.repository.CryptoRepository
import com.example.prueba.domain.model.Asset
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AssetsViewModel(private val repository: CryptoRepository) : ViewModel() {

    private val _assets = MutableStateFlow<List<Asset>>(emptyList())
    val assets: StateFlow<List<Asset>> = _assets

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _lastUpdated = MutableStateFlow<String?>(null)
    val lastUpdated: StateFlow<String?> = _lastUpdated

    init {
        fetchAssets()
    }

    private fun fetchAssets() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val assets = repository.getAssetsOnline()
                _assets.value = assets
                _lastUpdated.value = getCurrentDateTime()
            } catch (e: Exception) {
                _errorMessage.value = "Error loading assets. Showing offline data."
                val assets = repository.getAssetsOffline()
                _assets.value = assets
                _lastUpdated.value = repository.getLastUpdatedDate()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun saveDataForOffline() {
        viewModelScope.launch {
            try {
                repository.saveAssetsOffline(_assets.value)
                _lastUpdated.value = getCurrentDateTime()
            } catch (e: Exception) {
                _errorMessage.value = "Error saving data for offline use."
            }
        }
    }

    private fun getCurrentDateTime(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return dateFormat.format(Date())
    }
}
