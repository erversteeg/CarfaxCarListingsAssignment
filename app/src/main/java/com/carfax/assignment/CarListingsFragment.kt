package com.carfax.assignment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.carfax.assignment.adapter.CarListingsRecyclerViewAdapter
import com.carfax.assignment.databinding.FragmentCarListingsBinding
import com.carfax.assignment.repo.CarRepository
import com.carfax.assignment.viewmodel.CarViewModel
import kotlinx.coroutines.runBlocking


class CarListingsFragment : Fragment() {

    private var _binding: FragmentCarListingsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCarListingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = ViewModelProvider(this).get(CarViewModel::class.java)
        viewModel.loadAllCars()

        val recyclerView = binding.recyclerViewCarListings
        with(recyclerView) {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

            adapter = CarListingsRecyclerViewAdapter(this@CarListingsFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}