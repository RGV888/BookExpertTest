package com.pp.bookxpert.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: String,
    var name: String,
    val dataJson: String
)