package com.carfax.assignment.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.carfax.assignment.model.Car
import com.carfax.assignment.model.dao.CarDao


@Database(entities = [Car::class], version = 1, exportSchema = false)
abstract class CarDatabase: RoomDatabase() {

    abstract fun carDao(): CarDao

    companion object {
        private var instance: CarDatabase? = null

        fun getInstance(context: Context): CarDatabase {
            return if (instance != null) instance!!
            else {
                instance = Room.databaseBuilder(context.applicationContext,
                    CarDatabase::class.java, "car_database").build()

                instance!!
            }
        }
    }
}