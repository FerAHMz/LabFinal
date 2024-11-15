package com.example.prueba.data.network

import com.example.prueba.data.network.dto.AssetDto
import com.example.prueba.data.network.dto.AssetDetailDto
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class Api(private val client: HttpClient) {

    suspend fun getAssets(): List<AssetDto> {
        return client.get("https://api.coincap.io/v2/assets").body()
    }

    suspend fun getAssetDetails(id: String): AssetDetailDto {
        return client.get("https://api.coincap.io/v2/assets/$id").body()
    }
}
