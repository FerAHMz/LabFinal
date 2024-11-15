package com.example.prueba.data.repository

import com.example.prueba.data.local.dao.AssetDao
import com.example.prueba.data.mappers.toDomainModel
import com.example.prueba.data.mappers.toEntity
import com.example.prueba.data.mappers.toAssetDetail  // Asegúrate de tener esta importación
import com.example.prueba.data.network.Api
import com.example.prueba.domain.model.Asset
import com.example.prueba.domain.model.AssetDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CryptoRepository(
    private val api: Api,
    private val assetDao: AssetDao
) {
    suspend fun getAssetsOnline(): List<Asset> {
        val assetsDto = withContext(Dispatchers.IO) {
            api.getAssets()
        }
        return assetsDto.map { it.toDomainModel() }
    }

    suspend fun getAssetsOffline(): List<Asset> {
        val assetsEntity = withContext(Dispatchers.IO) {
            assetDao.getAllAssets()
        }
        return assetsEntity.map { it.toDomainModel() }
    }

    fun getLastUpdatedDate(): String {
        return "Última actualización: ${System.currentTimeMillis()}"
    }

    suspend fun saveAssetsOffline(assets: List<Asset>) {
        val assetsEntity = assets.map { it.toEntity() }
        withContext(Dispatchers.IO) {
            assetDao.insertAll(assetsEntity)
        }
    }

    suspend fun getAssetDetailsOnline(assetId: String): AssetDetail {
        val assetDetailDto = withContext(Dispatchers.IO) {
            api.getAssetDetails(assetId)
        }
        return assetDetailDto.toDomainModel()
    }

    suspend fun getAssetDetailsOffline(assetId: String): AssetDetail? {
        val assetEntity = withContext(Dispatchers.IO) {
            assetDao.getAssetById(assetId)
        }
        return assetEntity?.toAssetDetail()
    }
}
