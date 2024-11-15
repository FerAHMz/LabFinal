package com.example.prueba.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AssetDetailDto(
    val id: String,
    val name: String,
    val symbol: String,
    @SerialName("priceUsd") val priceUsd: Double,
    @SerialName("changePercent24Hr") val changePercent24Hr: Double,
    val supply: Double,
    @SerialName("maxSupply") val maxSupply: Double?,
    @SerialName("marketCapUsd") val marketCapUsd: Double,
    @SerialName("lastUpdated") val lastUpdated: Long
)
