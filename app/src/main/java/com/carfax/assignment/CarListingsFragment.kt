package com.carfax.assignment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.carfax.assignment.adapter.CarListingsRecyclerViewAdapter
import com.carfax.assignment.databinding.FragmentCarListingsBinding
import com.carfax.assignment.viewmodel.CarViewModel


class CarListingsFragment : Fragment(), CarListingsRecyclerViewAdapter.ContentListener {

    private var _binding: FragmentCarListingsBinding? = null
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

            val carListingsAdapter = CarListingsRecyclerViewAdapter(this@CarListingsFragment)

            carListingsAdapter.contentListener = this@CarListingsFragment

            adapter = carListingsAdapter

            addOnScrollListener(object: RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (recyclerView.computeVerticalScrollOffset() != 0) {
                        (activity as AppCompatActivity).supportActionBar?.title = ""
                    }
                    else {
                        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.company_name)
                    }
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onContent() {
        binding.noContentLayout.visibility = View.GONE
    }
}