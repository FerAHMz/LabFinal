package com.example.labfinal.data.mappers

import com.example.labfinal.data.local.entity.AssetDetailEntity
import com.example.labfinal.data.local.entity.AssetEntity
import com.example.labfinal.data.network.dto.AssetDto
import com.example.labfinal.data.network.dto.AssetDetailDto
import com.example.labfinal.domain.model.Asset
import com.example.labfinal.domain.model.AssetDetail

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
        id = this.id,
        name = this.name,
        symbol = this.symbol,
        priceUsd = this.priceUsd,
        changePercent24Hr = this.changePercent24Hr,
        supply = this.supply,
        maxSupply = this.maxSupply,
        marketCapUsd = this.marketCapUsd,
        lastUpdated = System.currentTimeMillis()
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

fun AssetDetailEntity.toAssetDetail(): AssetDetail {
    return AssetDetail(
        id = this.id,
        name = this.name,
        symbol = this.symbol,
        priceUsd = this.priceUsd,
        changePercent24Hr = this.changePercent24Hr,
        supply = this.supply,
        maxSupply = this.maxSupply,
        marketCapUsd = this.marketCapUsd,
        lastUpdated = this.lastUpdated
    )
}


