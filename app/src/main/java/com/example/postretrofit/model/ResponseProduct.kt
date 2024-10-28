package com.example.postretrofit.model

import com.google.gson.annotations.SerializedName

data class ResponseProduct(
	@field:SerializedName("nom")
	val nom: String? = null,
	@field:SerializedName("description")
	val description: String? = null,
	@field:SerializedName("prix")
	val prix: Int? = null,
	@field:SerializedName("images")
	val images: List<String?>? = null,
	@field:SerializedName("_id")
	val id: String? = null,
	@field:SerializedName("__v")
	val v: Int? = null,



	)
