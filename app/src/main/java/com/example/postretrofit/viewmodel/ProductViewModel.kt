package com.example.postretrofit.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.postretrofit.model.Product
import com.example.postretrofit.model.ResponseProduct
import com.example.postretrofit.network.ProductApi
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException


class ProductViewModel: ViewModel() {
    /** The mutable State that stores the status of the most recent request */
    var prodUiState = mutableStateOf<ResponseProduct?>(null)


    fun createProduct(product: Product) {
        viewModelScope.launch {
            try {
                val result = ProductApi.retrofitService.createProduct(product)
                prodUiState.value=result
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: HttpException) {
                e.printStackTrace()
            }
        }
    }
}