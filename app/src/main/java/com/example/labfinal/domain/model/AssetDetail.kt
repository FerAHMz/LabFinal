package com.example.prueba.domain.model

data class AssetDetail(
    val id: String,
    val name: String,
    val symbol: String,
    val priceUsd: Double,
    val changePercent24Hr: Double,
    val lastUpdated: Long,
    val supply: Double? = null,
    val maxSupply: Double? = null,
    val marketCapUsd: Double? = null
)
