package com.example.labfinal.di

import android.content.Context
import androidx.room.Room
import com.example.labfinal.data.local.CryptoDatabase
import com.example.labfinal.data.network.Api
import com.example.labfinal.data.repository.CryptoRepository
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

object ServiceLocator {

    private val httpClient: HttpClient by lazy {
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    prettyPrint = false
                })
            }

            install(Logging) {
                level = LogLevel.ALL
            }

            engine {
                requestTimeout = 10_000L
                endpoint {
                    connectTimeout = 10_000
                    socketTimeout = 10_000
                }
            }
        }
    }

    @Volatile
    private var database: CryptoDatabase? = null

    fun provideDatabase(context: Context): CryptoDatabase {
        return database ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                CryptoDatabase::class.java,
                "crypto_database"
            ).fallbackToDestructiveMigration()
                .build()
            database = instance
            instance
        }
    }

    private val api: Api by lazy {
        Api(httpClient)
    }

    fun provideCryptoRepository(context: Context): CryptoRepository {
        val dao = provideDatabase(context).assetDao()
        return CryptoRepository(api, dao)
    }
}
