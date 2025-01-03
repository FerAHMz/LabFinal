package com.example.labfinal.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.labfinal.data.repository.CryptoRepository

class AssetDetailViewModelFactory(
    private val repository: CryptoRepository,
    private val assetId: String
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AssetDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AssetDetailViewModel(repository, assetId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
