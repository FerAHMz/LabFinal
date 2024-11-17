package com.example.labfinal.domain.model

data class Asset(
    val id: String,
    val name: String,
    val symbol: String,
    val priceUsd: Double,
    val changePercent24Hr: Double,
    val lastUpdated: Long = System.currentTimeMillis()
)
