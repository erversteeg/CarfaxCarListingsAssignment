package com.carfax.assignment.adapter

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.carfax.assignment.R
import com.carfax.assignment.model.Car
import com.carfax.assignment.viewmodel.CarViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers


class CarListingsRecyclerViewAdapter(fragment: Fragment): RecyclerView.Adapter<CarListingsRecyclerViewAdapter.CarViewHolder>() {

    private var cars = emptyList<Car>()

    init {
        val viewModel = ViewModelProvider(fragment).get(CarViewModel::class.java)

        viewModel.allCars.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe { cars ->
            val sIndex = this.cars.size
            val count = cars.size - this.cars.size

            notifyItemRangeInserted(sIndex, count)

            this.cars = cars
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_car_cell, parent, false)
        return CarViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        val car = cars[position]

        holder.titleTextView.text = "${car.make} ${car.model}"
    }

    override fun getItemCount(): Int {
        return cars.size
    }

    class CarViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val titleTextView: TextView = view.findViewById(R.id.text_view_car_title)
    }
}