package com.pp.bookxpert

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.pp.bookxpert.daos.ProductDao
import com.pp.bookxpert.daos.UserDao
import com.pp.bookxpert.models.ProductEntity
import com.pp.bookxpert.models.UserEntity
import com.pp.bookxpert.utils.Converters

@Database(entities = [ProductEntity::class, UserEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao

    abstract fun userDao(): UserDao
}

