package com.example.prueba.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.prueba.data.local.entity.AssetEntity

@Dao
interface AssetDao {

    @Query("SELECT * FROM assets")
    suspend fun getAllAssets(): List<AssetEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(assets: List<AssetEntity>)

    @Query("SELECT * FROM assets WHERE id = :assetId LIMIT 1")
    suspend fun getAssetById(assetId: String): AssetEntity?
}
