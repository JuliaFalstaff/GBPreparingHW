package com.example.gbpreparinghw.ui

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.gbpreparinghw.R
import com.example.gbpreparinghw.databinding.FragmentMapsBinding
import com.example.gbpreparinghw.model.repo.AppState
import com.example.gbpreparinghw.utils.toEntity
import com.example.gbpreparinghw.viewmodel.MapsViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.IOException

class MapsFragment : Fragment() {

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!
    private val markersList: ArrayList<Marker> = arrayListOf()
    private lateinit var map: GoogleMap
    private val mapViewModel: MapsViewModel by viewModel()

    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap
        val initialPlace = LatLng(LONG_LONDON_INITIAL, LAT_LONDON_INITIAL)
        googleMap.addMarker(
            MarkerOptions().position(initialPlace)
                .title(getString(R.string.google_map_initial_place))
        )
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(initialPlace))
        googleMap.setOnMapLongClickListener {
            getAddressAsync(it)
            addMarkerToArray(it)

        }
        activateMyLocation(googleMap)
    }

    private fun addMarkerToArray(location: LatLng) {
        val marker = setMarker(location, getString(R.string.titleMarkerList), getString(R.string.descriptionMarkerList))
        marker?.let {
            markersList.add(it)
            mapViewModel.addMarker(it.toEntity())
        }

        mapViewModel.addAll(markersList.map {
            it.toEntity()
        })

    }

    private fun setMarker(
        location: LatLng,
        searchText: String,
        snippetText: String
    ): Marker? {
        return map.addMarker(
            MarkerOptions()
                .position(location)
                .title(searchText)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_pin))
                .snippet(snippetText)
        )
    }

    private fun getAddressAsync(location: LatLng) {
        requireContext().let {
            val geocoder = Geocoder(it)
            Thread {
                try {
                    val address = geocoder.getFromLocation(
                        location.latitude,
                        location.longitude,
                        MAX_RESULT_ADDRESS
                    )
                    binding.textAddressMap.post {
                        binding.textAddressMap.text = address.first().getAddressLine(0)
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }.start()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.googleMap) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        initSearchByAddress()
        mapViewModel.getData().observe(viewLifecycleOwner, Observer { renderData(it) })
        mapViewModel.getAllMarkers()
    }

    fun renderData(state: AppState) {
        when (state) {
            is AppState.Success -> {
                val response = state.markersData
                response.forEach {
                    map.clear()
                    map.addMarker(it)
                    Log.d("tag", "marker $it")
                }
            }
        }
    }

    private fun initSearchByAddress() {
        binding.buttonSearchMap.setOnClickListener {
            val geoCoder = Geocoder(it.context)
            val searchText = binding.searchAddressMap.text.toString()
            Thread {
                try {
                    val address = geoCoder.getFromLocationName(searchText, MAX_RESULT_ADDRESS)
                    if (address.size > 0) {
                        goToAddress(address, it, searchText)
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }.start()
        }
    }

    private fun goToAddress(address: MutableList<Address>, view: View?, searchText: String) {
        val location = LatLng(address.first().latitude, address.first().longitude)
        view?.post {
            setMarker(location, searchText, searchText)
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, CAMERA_ZOOM))
        }
    }

    private fun activateMyLocation(googleMap: GoogleMap) {
        requireContext().let {
            when (PackageManager.PERMISSION_GRANTED) {
                ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                -> {
                    googleMap.isMyLocationEnabled = true
                    googleMap.uiSettings.isMyLocationButtonEnabled = true
                }
                else -> {
                    requestPermission()
                }
            }
        }
    }

    private fun requestPermission() {
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                activateMyLocation(map)
            } else {
                requireActivity().let {
                    AlertDialog.Builder(it)
                        .setTitle(getString(R.string.dialog_rationale_title))
                        .setMessage(getString(R.string.dialog_rationale_message))
                        .setNegativeButton(getString(R.string.dialog_neutral_button)) { dialog, _ ->
                            dialog.dismiss()
                        }
                        .create()
                        .show()
                }
            }
        }


    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    companion object {
        fun newInstance() = MapsFragment()
        private const val CAMERA_ZOOM = 15f
        private const val LONG_LONDON_INITIAL = 51.0
        private const val LAT_LONDON_INITIAL = -0.12
        private const val MAX_RESULT_ADDRESS = 1
    }
}