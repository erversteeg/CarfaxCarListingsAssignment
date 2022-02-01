package com.carfax.assignment.repository

import android.content.Context
import android.util.Log
import com.carfax.assignment.database.CarDatabase
import com.carfax.assignment.model.Car
import com.carfax.assignment.model.dao.CarDao
import com.carfax.assignment.network.service.CarListingsService
import com.google.gson.JsonObject
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class CarRepository(context: Context) {

    private val apiUrl = "https://carfax-for-consumers.firebaseio.com/"

    private var retrofit: Retrofit = Retrofit.Builder().baseUrl(apiUrl).addConverterFactory(GsonConverterFactory.create()).build()
    private var carListingsService: CarListingsService = retrofit.create(CarListingsService::class.java)

    private val carDao: CarDao

    val allCars: Flowable<List<Car>>

    init {
        val database by lazy { CarDatabase.getInstance(context) }
        carDao = database.carDao()

        allCars = carDao.getAllCars()
    }

    fun insert(car: Car) {
        Completable.fromRunnable {
            carDao.insert(car)
        }.subscribeOn(Schedulers.io()).subscribe()
    }

    fun getCar(uid: Int): Flowable<Car> {
        return carDao.getCar(uid)
    }

    fun loadAllCars() {
        Completable.fromRunnable {
            val cached = carDao.getAnyCar() != null

            if (!cached) {
                val call = carListingsService.getCarfaxResponse()

                call.enqueue(object : Callback<JsonObject> {
                    override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                        val listings = response.body()!!.getAsJsonArray("listings")
                        for (listingElement in listings) {
                            val listing = listingElement.asJsonObject

                            val photoUrl = listing.get("images").asJsonObject.get("large").asString
                            val year = listing.get("year").asInt
                            val make = listing.get("make").asString
                            val model = listing.get("model").asString
                            val trim = listing.get("trim").asString
                            val price = listing.get("currentPrice").asFloat
                            val mileage = listing.get("mileage").asInt

                            val dealer = listing.get("dealer").asJsonObject
                            val dealerPhoneNumber = dealer.get("phone").asString
                            val dealerCity = dealer.get("city").asString
                            val dealerState = dealer.get("state").asString

                            val interiorColor = listing.get("interiorColor").asString
                            val exteriorColor = listing.get("exteriorColor").asString
                            val driveType = listing.get("drivetype").asString
                            val transmission = listing.get("transmission").asString
                            val engine = listing.get("engine").asString
                            val bodyType = listing.get("bodytype").asString
                            val fuel = listing.get("fuel").asString

                            insert(Car(0, photoUrl, year, make, model, trim, price, mileage,
                                dealerPhoneNumber, dealerCity, dealerState, interiorColor,
                                exteriorColor, driveType, transmission, engine, bodyType, fuel))
                        }
                    }

                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                        Log.i("Error", "Error")
                    }
                })
            }
        }.subscribeOn(Schedulers.io()).subscribe()
    }
}