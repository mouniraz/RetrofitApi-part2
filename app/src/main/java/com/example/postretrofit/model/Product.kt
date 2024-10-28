package com.example.postretrofit.model

import com.google.gson.annotations.SerializedName

data class Product(

	@field:SerializedName("nom")
	val nom: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("prix")
	val prix: Int? = null,


	@field:SerializedName("images")
    val images: List<String?>? = null
)
