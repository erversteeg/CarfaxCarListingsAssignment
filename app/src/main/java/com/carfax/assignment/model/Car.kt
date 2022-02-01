package com.carfax.assignment.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cars")
data class Car(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "photo_url") val photoUrl: String,
    @ColumnInfo(name = "year") val year: Int,
    @ColumnInfo(name = "make") val make: String,
    @ColumnInfo(name = "model") val model: String,
    @ColumnInfo(name = "trim") val trim: String,
    @ColumnInfo(name = "price") val price: Float,
    @ColumnInfo(name = "mileage") val mileage: Int,
    @ColumnInfo(name = "dealer_phone") val dealerPhoneNumber: String,
    @ColumnInfo(name = "dealer_city") val dealerCity: String,
    @ColumnInfo(name = "dealer_state") val dealerState: String,
    @ColumnInfo(name = "interior_color") val interiorColor: String,
    @ColumnInfo(name = "exterior_color") val exteriorColor: String,
    @ColumnInfo(name = "drive_type") val driveType: String,
    @ColumnInfo(name = "transmission") val transmission: String,
    @ColumnInfo(name = "engine") val engine: String,
    @ColumnInfo(name = "body_type") val bodyType: String,
    @ColumnInfo(name = "fuel") val fuel: String
)