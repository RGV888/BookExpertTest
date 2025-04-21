package com.pp.bookxpert.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pp.bookxpert.models.ProductEntity
import com.pp.bookxpert.repo.ProductRepository
import com.pp.bookxpert.statehanler.UiState
import com.pp.bookxpert.utils.NetworkUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val repository: ProductRepository,
    application: Application
) : AndroidViewModel(application) { // ← change ViewModel to AndroidViewModel

    private val context = application.applicationContext

    private val _productState = MutableStateFlow<UiState<List<ProductEntity>>>(UiState.Loading)
    val productState: StateFlow<UiState<List<ProductEntity>>> = _productState

    fun loadProducts() {
        viewModelScope.launch {
            _productState.value = UiState.Loading

            val local = repository.getLocalProducts()

            if (local.isNotEmpty() ) {
                _productState.value = UiState.Success(local)
                return@launch
            }

            val isConnected = NetworkUtils.isNetworkAvailable(context)
            if (!isConnected) {
                _productState.value = UiState.Error("No internet connection and No data available")
                return@launch
            }

            val result = repository.fetchAndCacheProducts()
            result.onSuccess {
                _productState.value = UiState.Success(it)
            }.onFailure {
                if (local.isNotEmpty()) {
                    _productState.value = UiState.Success(local)
                } else {
                    _productState.value = UiState.Error(it.message ?: "Something went wrong")
                }
            }
        }
    }

    fun updateProduct(product: ProductEntity) {
        viewModelScope.launch {
            repository.updateProduct(product)
            loadProducts()
        }
    }

    fun deleteProduct(product: ProductEntity) {
        viewModelScope.launch {
            repository.deleteProduct(product)
            loadProducts()
        }
    }
}


//revstring

fun RevString(){

    var name="Prasad"
    var revStr=""
    for (index in name.length-1 downTo 0){
        revStr += revStr[index]
    }

    print(revStr)

}



//@HiltViewModel
//class ProductViewModel @Inject constructor(
//    private val repository: ProductRepository
//) : ViewModel() {
//
//    private val _productState = MutableStateFlow<UiState<List<ProductEntity>>>(UiState.Loading)
//    val productState: StateFlow<UiState<List<ProductEntity>>> = _productState.asStateFlow()
//
//    fun loadProducts() {
//        viewModelScope.launch {
//            _productState.value = UiState.Loading
//            val result = repository.fetchAndCacheProducts()
//            result.onSuccess {
//                _productState.value = UiState.Success(it)
//            }.onFailure {
//                val local = repository.getLocalProducts()
//                if (local.isNotEmpty()) {
//                    _productState.value = UiState.Success(local)
//                } else {
//                    _productState.value = UiState.Error(it.message ?: "Unknown error")
//                }
//            }
//        }
//    }
//
//    fun updateProduct(product: ProductEntity) {
//        viewModelScope.launch {
//            repository.updateProduct(product)
//            loadProducts()
//        }
//    }
//
//    fun deleteProduct(product: ProductEntity) {
//        viewModelScope.launch {
//            repository.deleteProduct(product)
//            loadProducts()
//        }
//    }
//}
