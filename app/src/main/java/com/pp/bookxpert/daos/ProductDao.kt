package com.pp.bookxpert.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.pp.bookxpert.models.ProductEntity

@Dao
interface ProductDao {
    @Query("SELECT * FROM products")
    suspend fun getAllProducts(): List<ProductEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<ProductEntity>)

    @Update
    suspend fun updateProduct(product: ProductEntity)

    @Delete
    suspend fun deleteProduct(product: ProductEntity)
}