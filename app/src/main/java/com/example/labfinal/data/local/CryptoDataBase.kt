    package com.example.prueba.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.prueba.data.local.dao.AssetDao
import com.example.prueba.data.local.entity.AssetEntity

@Database(entities = [AssetEntity::class], version = 1, exportSchema = false)
abstract class CryptoDatabase : RoomDatabase() {
    abstract fun assetDao(): AssetDao
}
