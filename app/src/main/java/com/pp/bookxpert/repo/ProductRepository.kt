package com.pp.bookxpert.repo

import com.google.gson.Gson
import com.pp.bookxpert.daos.ProductDao
import com.pp.bookxpert.models.ProductEntity
import com.pp.bookxpert.network.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepository @Inject constructor(
    private val api: ApiService,
    private val dao: ProductDao
) {
    suspend fun fetchAndCacheProducts(): Result<List<ProductEntity>> {
        return try {
            val response = api.fetchProducts()

            val entityList = response.map { item ->
                val json = Gson().toJson(item.data) // convert map to json string

                ProductEntity(
                    id = item.id,
                    name = item.name,
                    dataJson = json
                )
            }



            dao.insertProducts(entityList)
            Result.success(entityList)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getLocalProducts(): List<ProductEntity> = dao.getAllProducts()

    suspend fun updateProduct(product: ProductEntity) = dao.updateProduct(product)

    suspend fun deleteProduct(product: ProductEntity) = dao.deleteProduct(product)
}