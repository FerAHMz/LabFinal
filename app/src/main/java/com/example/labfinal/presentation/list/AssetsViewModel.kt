package com.example.labfinal.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.labfinal.data.repository.CryptoRepository
import com.example.labfinal.domain.model.Asset
import com.example.labfinal.domain.network.util.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AssetsViewModel(private val repository: CryptoRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Asset>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Asset>>> = _uiState

    private val _lastUpdated = MutableStateFlow<String?>("No disponible")
    val lastUpdated: StateFlow<String?> = _lastUpdated

    init {
        fetchAssets()
    }

    fun fetchAssets() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val onlineAssets = repository.getAssetsOnline()
                _uiState.value = UiState.Success(onlineAssets)
                _lastUpdated.value = repository.getLastUpdatedDate()
                repository.saveAssetsOffline(onlineAssets)
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Error cargando datos en línea. Mostrando datos offline si están disponibles.")
                val offlineAssets = repository.getAssetsOffline()
                if (offlineAssets.isEmpty()) {
                    _uiState.value = UiState.Error("No hay datos offline disponibles.")
                } else {
                    _uiState.value = UiState.Success(offlineAssets)
                    _lastUpdated.value = "Última actualización offline: ${getCurrentDateTime()}"
                }
            }
        }
    }

    fun saveDataForOffline() {
        viewModelScope.launch {
            try {
                val currentAssets = (_uiState.value as? UiState.Success)?.data ?: emptyList()
                repository.saveAssetsOffline(currentAssets)
                _lastUpdated.value = "Guardado para uso offline: ${getCurrentDateTime()}"
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Error guardando datos offline: ${e.message}")
            }
        }
    }

    private fun getCurrentDateTime(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return dateFormat.format(Date())
    }
}
