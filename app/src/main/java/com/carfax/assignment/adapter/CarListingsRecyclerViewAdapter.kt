package com.carfax.assignment.adapter

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.carfax.assignment.R
import com.carfax.assignment.model.Car
import com.carfax.assignment.viewmodel.CarViewModel
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.text.NumberFormat
import kotlin.math.roundToInt


class CarListingsRecyclerViewAdapter(private val fragment: Fragment): RecyclerView.Adapter<CarListingsRecyclerViewAdapter.CarViewHolder>() {

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

        Glide.with(fragment).load(car.photoUrl).placeholder(R.drawable.downloading).error(R.drawable.broken_image).centerCrop().into(holder.largePhotoImageView)

        holder.yearMakeModelTextView.text = fragment.getString(R.string.year_make_model_format, car.year, car.make, car.model)
        holder.priceTextView.text = fragment.getString(R.string.localized_price_format, NumberFormat.getInstance().format(car.price.roundToInt()))
        holder.mileageTextView.text = fragment.getString(R.string.mileage_format, car.mileage.toFloat() / 1000)
        holder.locationTextView.text = fragment.getString(R.string.location_format, car.dealerCity, car.dealerState)

        holder.callDealerButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(fragment.requireContext(), android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(Intent.ACTION_CALL, Uri.parse(fragment.getString(R.string.call_phone_format, car.dealerPhoneNumber)))
                fragment.requireActivity().startActivity(intent)
            }
            else {
                val intent = Intent(Intent.ACTION_CALL, Uri.parse(fragment.getString(R.string.call_phone_format, car.dealerPhoneNumber)))
                fragment.requireActivity().startActivity(intent)
            }
        }

        holder.itemView.setOnClickListener { view ->
            fragment.findNavController().navigate(R.id.action_car_details_fragment, Bundle().apply {
                putInt("car_id", car.uid)
            })
        }
    }

    override fun getItemCount(): Int {
        return cars.size
    }

    class CarViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val largePhotoImageView: ImageView = view.findViewById(R.id.image_view_photo_large)

        val yearMakeModelTextView: TextView = view.findViewById(R.id.text_view_year_make_model)
        val priceTextView: TextView = view.findViewById(R.id.text_view_price)
        val mileageTextView: TextView = view.findViewById(R.id.text_view_mileage)
        val locationTextView: TextView = view.findViewById(R.id.text_view_location)

        val callDealerButton: Button = view.findViewById(R.id.button_call_dealer)
    }
}