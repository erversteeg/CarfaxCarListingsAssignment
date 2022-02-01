package com.carfax.assignment.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.carfax.assignment.model.Car
import com.carfax.assignment.repository.CarRepository
import io.reactivex.rxjava3.core.Flowable


class CarViewModel(application: Application): AndroidViewModel(application) {
    private val repository by lazy { CarRepository(application) }

    val allCars: Flowable<List<Car>> = repository.allCars

    fun getCar(uid: Int): Flowable<Car> {
        return repository.getCar(uid)
    }

    fun loadAllCars() {
        repository.loadAllCars()
    }
}