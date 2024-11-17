package com.example.labfinal.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "asset_detail")
data class AssetDetailEntity(
    @PrimaryKey val id: String,
    val name: String,
    val symbol: String,
    val priceUsd: Double,
    val changePercent24Hr: Double,
    val supply: Double?,
    val maxSupply: Double?,
    val marketCapUsd: Double?,
    val lastUpdated: Long
)
