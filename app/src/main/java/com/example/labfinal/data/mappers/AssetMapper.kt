package com.example.prueba.data.mappers

import com.example.prueba.data.local.entity.AssetEntity
import com.example.prueba.data.network.dto.AssetDto
import com.example.prueba.data.network.dto.AssetDetailDto
import com.example.prueba.domain.model.Asset
import com.example.prueba.domain.model.AssetDetail

fun AssetDto.toDomainModel(): Asset {
    return Asset(
        id = id,
        name = name,
        symbol = symbol,
        priceUsd = priceUsd.toDoubleOrNull() ?: 0.0,
        changePercent24Hr = changePercent24Hr.toDoubleOrNull() ?: 0.0,
        lastUpdated = System.currentTimeMillis()
    )
}

fun AssetDetailDto.toDomainModel(): AssetDetail {
    return AssetDetail(
        id = id,
        name = name,
        symbol = symbol,
        priceUsd = priceUsd,
        changePercent24Hr = changePercent24Hr,
        supply = supply,
        maxSupply = maxSupply,
        marketCapUsd = marketCapUsd,
        lastUpdated = lastUpdated
    )
}

fun Asset.toEntity(): AssetEntity {
    return AssetEntity(
        id = id,
        name = name,
        symbol = symbol,
        priceUsd = priceUsd,
        changePercent24Hr = changePercent24Hr,
        lastUpdated = lastUpdated,
        supply = null,
        maxSupply = null,
        marketCapUsd = null
    )
}

fun AssetEntity.toDomainModel(): Asset {
    return Asset(
        id = id,
        name = name,
        symbol = symbol,
        priceUsd = priceUsd,
        changePercent24Hr = changePercent24Hr,
        lastUpdated = lastUpdated
    )
}

fun AssetEntity.toAssetDetail(): AssetDetail {
    return AssetDetail(
        id = id,
        name = name,
        symbol = symbol,
        priceUsd = priceUsd,
        changePercent24Hr = changePercent24Hr,
        lastUpdated = lastUpdated,
        supply = supply,
        maxSupply = maxSupply,
        marketCapUsd = marketCapUsd
    )
}
