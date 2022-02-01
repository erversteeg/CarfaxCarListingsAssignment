package com.carfax.assignment.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.carfax.assignment.model.Car
import io.reactivex.rxjava3.core.Flowable


@Dao
interface CarDao {

    @Query("SELECT * FROM cars LIMIT 1")
    fun getAnyCar(): Car?

    @Query("SELECT * FROM cars")
    fun getAllCars(): Flowable<List<Car>>

    @Insert
    fun insert(car: Car)
}