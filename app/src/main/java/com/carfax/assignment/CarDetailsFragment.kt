package com.carfax.assignment

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.carfax.assignment.databinding.FragmentCarDetailsBinding
import com.carfax.assignment.model.Car
import com.carfax.assignment.viewmodel.CarViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.text.NumberFormat
import kotlin.math.roundToInt


class CarDetailsFragment : Fragment() {

    private var _binding: FragmentCarDetailsBinding? = null

    private val binding get() = _binding!!

    var carId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.apply {
            carId = getInt("car_id")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCarDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = ViewModelProvider(this).get(CarViewModel::class.java)

        viewModel.getCar(carId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe { car ->
            showCarDetails(car)
        }
    }

    private fun showCarDetails(car: Car) {
        (activity as AppCompatActivity).supportActionBar?.title = "${car.year} ${car.make} ${car.model}"

        Glide.with(this).load(car.photoUrl).placeholder(R.drawable.downloading)
            .error(R.drawable.broken_image).centerCrop().into(binding.imageViewPhotoLarge)

        binding.textViewYearMakeModel.text = "${car.year} ${car.make} ${car.model}"
        binding.textViewPrice.text = "\$${NumberFormat.getInstance().format(car.price.roundToInt())}"
        binding.textViewMileage.text = "${String.format("%.1fk mi", car.mileage.toFloat() / 1000)}"

        binding.textViewLocation.text = "${car.dealerCity}, ${car.dealerState}"
        binding.textViewExteriorColor.text = car.exteriorColor
        binding.textViewInteriorColor.text = car.interiorColor
        binding.textViewDriveType.text = car.driveType
        binding.textViewTransmission.text = car.transmission
        binding.textViewBodyStyle.text = car.bodyType
        binding.textViewEngine.text = car.engine
        binding.textViewFuel.text = car.fuel

        binding.buttonCallDealer.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel: ${car.dealerPhoneNumber}"))
                requireActivity().startActivity(intent)
            }
            else {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel: ${car.dealerPhoneNumber}"))
                requireActivity().startActivity(intent)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}