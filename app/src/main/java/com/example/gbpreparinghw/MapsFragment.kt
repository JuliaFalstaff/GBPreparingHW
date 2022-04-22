package com.example.gbpreparinghw

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.gbpreparinghw.databinding.FragmentMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException

class MapsFragment : Fragment() {

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!
    private val markersList: ArrayList<Marker> = arrayListOf()
    private lateinit var map: GoogleMap

    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap
        val initialPlace = LatLng(LONG_LONDON_INITIAL, LAT_LONDON_INITIAL)
        googleMap.addMarker(MarkerOptions().position(initialPlace).title(getString(R.string.google_map_initial_place)))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(initialPlace))
        googleMap.setOnMapLongClickListener {
            getAddressAsync(it)
            addMarkerToArray(it)
        }
    }

    private fun addMarkerToArray(location: LatLng) {
        val marker = setMarker(location, markersList.size.toString(), R.drawable.ic_map_pin)
        marker?.let { markersList.add(it) }
    }

    private fun setMarker(location: LatLng, searchText: String, icMapPin: Int): Marker? {
        return map.addMarker(
            MarkerOptions()
                .position(location)
                .title(searchText)
                .icon(BitmapDescriptorFactory.fromResource(icMapPin))
        )
    }

    private fun getAddressAsync(location: LatLng) {
        requireContext().let {
            val geocoder = Geocoder(it)
            Thread {
                try {
                    val address = geocoder.getFromLocation(location.latitude, location.longitude, MAX_RESULT_ADDRESS)
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
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.googleMap) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        initSearchByAddress()
    }

    private fun initSearchByAddress() {
        binding.buttonSearchMap.setOnClickListener {
            val geoCoder = Geocoder(it.context)
            val searchText = binding.searchAddressMap.text.toString()
            Thread {
                try {
                    val address = geoCoder.getFromLocationName(searchText, MAX_RESULT_ADDRESS)
                    if(address.size > 0) {
                        goToAddress(address, it, searchText)
                    }
                }catch (e: IOException) {
                    e.printStackTrace()
                }
            }.start()
        }
    }

    private fun goToAddress(address: MutableList<Address>, view: View?, searchText: String) {
        val location = LatLng(address.first().latitude, address.first().longitude)
        view?.post {
            setMarker(location, searchText, R.drawable.ic_map_pin)
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, CAMERA_ZOOM))
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