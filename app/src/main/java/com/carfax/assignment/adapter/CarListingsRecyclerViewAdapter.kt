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
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.text.NumberFormat
import kotlin.math.roundToInt


class CarListingsRecyclerViewAdapter(private val fragment: Fragment): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface ContentListener {
        fun onContent()
    }

    var contentListener: ContentListener? = null

    private var cars = emptyList<Car>()

    private val viewTypeCar = 0
    private val viewTypeFooter = 1

    init {
        val viewModel = ViewModelProvider(fragment).get(CarViewModel::class.java)

        viewModel.allCars.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe { cars ->
            if (cars.isNotEmpty()) {
                contentListener?.onContent()
            }

            val sIndex = this.cars.size
            val count = cars.size - this.cars.size

            notifyItemRangeInserted(sIndex, count)

            this.cars = cars
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == viewTypeCar) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_car_cell, parent, false)
            CarViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_footer_cell, parent, false)
            FooterViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position < itemCount - 1) {
            val car = cars[position]

            val carViewHolder = holder as CarViewHolder

            Glide.with(fragment).load(car.photoUrl).placeholder(R.drawable.ic_downloading).error(R.drawable.ic_broken_image).centerCrop().into(carViewHolder.largePhotoImageView)

            carViewHolder.yearMakeModelTextView.text = fragment.getString(R.string.year_make_model_trim_format, car.year, car.make, car.model, car.trim)
            carViewHolder.priceTextView.text = fragment.getString(R.string.localized_price_format, NumberFormat.getInstance().format(car.price.roundToInt()))
            carViewHolder.mileageTextView.text = fragment.getString(R.string.mileage_format, car.mileage.toFloat() / 1000)
            carViewHolder.locationTextView.text = fragment.getString(R.string.location_format, car.dealerCity, car.dealerState)

            carViewHolder.callDealerButton.setOnClickListener {
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

    }

    override fun getItemCount(): Int {
        return cars.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < itemCount - 1) {
            viewTypeCar
        }
        else {
            viewTypeFooter
        }
    }

    class CarViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val largePhotoImageView: ImageView = view.findViewById(R.id.image_view_photo_large)

        val yearMakeModelTextView: TextView = view.findViewById(R.id.text_view_year_make_model)
        val priceTextView: TextView = view.findViewById(R.id.text_view_price)
        val mileageTextView: TextView = view.findViewById(R.id.text_view_mileage)
        val locationTextView: TextView = view.findViewById(R.id.text_view_location)

        val callDealerButton: Button = view.findViewById(R.id.button_call_dealer)
    }

    class FooterViewHolder(view: View): RecyclerView.ViewHolder(view) {

    }
}