package com.example.labfinal.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.labfinal.data.local.entity.AssetDetailEntity

@Dao
interface AssetDetailDao {
    @Query("SELECT * FROM asset_detail WHERE id = :assetId")
    suspend fun getAssetById(assetId: String): AssetDetailEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAsset(asset: AssetDetailEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllAssets(assets: List<AssetDetailEntity>)
}
