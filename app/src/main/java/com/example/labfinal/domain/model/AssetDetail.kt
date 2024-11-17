package com.example.labfinal.domain.model

data class AssetDetail(
    val id: String,
    val name: String,
    val symbol: String,
    val priceUsd: Double,
    val changePercent24Hr: Double,
    val supply: Double?,
    val maxSupply: Double?,
    val marketCapUsd: Double?,
    val lastUpdated: Long
)
