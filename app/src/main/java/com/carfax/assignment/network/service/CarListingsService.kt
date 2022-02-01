package com.carfax.assignment.network.service

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.GET

interface CarListingsService {
    @GET("assignment.json")
    fun getCarfaxResponse(): Call<JsonObject>
}