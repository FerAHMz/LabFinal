package com.example.labfinal.data.repository

import com.example.labfinal.data.local.dao.AssetDao
import com.example.labfinal.data.mappers.toDomainModel
import com.example.labfinal.data.mappers.toEntity
import com.example.labfinal.data.mappers.toAssetDetail
import com.example.labfinal.data.network.Api
import com.example.labfinal.domain.model.Asset
import com.example.labfinal.domain.model.AssetDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class CryptoRepository(
    private val api: Api,
    private val assetDao: AssetDao
) {

    suspend fun getAssetsOnline(): List<Asset> {
        return withContext(Dispatchers.IO) {
            try {
                val assetsDto = api.getAssets()
                assetsDto.map { it.toDomainModel() }
            } catch (e: Exception) {
                throw Exception("Error al obtener datos online: ${e.localizedMessage}")
            }
        }
    }

    suspend fun getAssetsOffline(): List<Asset> {
        return withContext(Dispatchers.IO) {
            val assetsEntity = assetDao.getAllAssets()
            assetsEntity.map { it.toDomainModel() }
        }
    }

    fun getLastUpdatedDate(): String {
        val currentTimeMillis = System.currentTimeMillis()
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return "Última actualización: ${formatter.format(Date(currentTimeMillis))}"
    }

    suspend fun saveAssetsOffline(assets: List<Asset>) {
        withContext(Dispatchers.IO) {
            val assetsEntity = assets.map { it.toEntity() }
            assetDao.insertAll(assetsEntity)
        }
    }

    suspend fun getAssetDetailsOnline(assetId: String): AssetDetail {
        return withContext(Dispatchers.IO) {
            try {
                val assetDetailDto = api.getAssetDetails(assetId)
                assetDetailDto.toDomainModel()
            } catch (e: Exception) {
                throw Exception("Error al obtener detalles del asset online: ${e.localizedMessage}")
            }
        }
    }

    suspend fun getAssetDetailsOffline(assetId: String): AssetDetail? {
        return withContext(Dispatchers.IO) {
            val assetEntity = assetDao.getAssetById(assetId)
            assetEntity?.toAssetDetail()
        }
    }
}
