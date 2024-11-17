package com.example.labfinal.data.network

import com.example.labfinal.data.network.dto.AssetDto
import com.example.labfinal.data.network.dto.AssetDetailDto
import com.example.labfinal.data.network.dto.ApiResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class Api(private val client: HttpClient) {

    suspend fun getAssets(): List<AssetDto> {
        return try {
            val response: ApiResponse<List<AssetDto>> = client.get("https://api.coincap.io/v2/assets").body()
            response.data
        } catch (e: Exception) {
            throw RuntimeException("Error al obtener datos de los assets: ${e.message}")
        }
    }

    suspend fun getAssetDetails(id: String): AssetDetailDto {
        return try {
            val response: ApiResponse<AssetDetailDto> = client.get("https://api.coincap.io/v2/assets/$id").body()
            response.data
        } catch (e: Exception) {
            throw RuntimeException("Error al obtener detalles del asset: ${e.message}")
        }
    }
}
