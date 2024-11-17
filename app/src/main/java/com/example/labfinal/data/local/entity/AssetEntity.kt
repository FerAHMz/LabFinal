package com.example.labfinal.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.labfinal.domain.model.Asset

@Entity(tableName = "assets")
data class AssetEntity(
    @PrimaryKey val id: String,
    val name: String,
    val symbol: String,
    val priceUsd: Double,
    val changePercent24Hr: Double,
    val lastUpdated: Long,
    val supply: Double?,
    val maxSupply: Double?,
    val marketCapUsd: Double?
)
