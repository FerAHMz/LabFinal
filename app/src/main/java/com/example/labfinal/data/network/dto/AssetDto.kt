package com.example.labfinal.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AssetDto(
    val id: String,
    val name: String,
    val symbol: String,
    @SerialName("priceUsd") val priceUsd: String,
    @SerialName("changePercent24Hr") val changePercent24Hr: String
)

