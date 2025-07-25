package com.example.neednet

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import java.util.*

class LocationPickerActivity : AppCompatActivity(), MapEventsReceiver {

    private lateinit var map: MapView
    private lateinit var mapController: IMapController
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var marker: Marker
    private var myLocationOverlay: MyLocationNewOverlay? = null
    private lateinit var editAddress: EditText

    companion object {
        private const val LOCATION_PERMISSION_REQUEST = 101
        private val DEFAULT_LOCATION = GeoPoint(22.5937, 78.9629) // Center of India
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Configuration.getInstance().load(this, getSharedPreferences("osmdroid", MODE_PRIVATE))
        setContentView(R.layout.activity_location_picker)

        editAddress = findViewById(R.id.editAddress)
        map = findViewById(R.id.map)
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.setMultiTouchControls(true)
        mapController = map.controller
        mapController.setZoom(5.0) // Initial zoom out

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        marker = Marker(map).apply {
            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        }
        map.overlays.add(marker)

        // Add click listener overlay
        val eventsOverlay = MapEventsOverlay(this)
        map.overlays.add(eventsOverlay)

        findViewById<Button>(R.id.btnUseCurrentLocation).setOnClickListener {
            getCurrentLocation()
        }

        findViewById<Button>(R.id.btnSelectLocation).setOnClickListener {
            val geo = marker.position
            val address = getAddressFromLatLng(geo.latitude, geo.longitude)
            val resultIntent = Intent().apply {
                putExtra("selected_address", address)
                putExtra("latitude", geo.latitude)
                putExtra("longitude", geo.longitude)
            }
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

        requestLocationPermissionIfNeeded()
    }

    private fun requestLocationPermissionIfNeeded() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST
            )
        } else {
            setupMyLocationOverlay()
            getCurrentLocation()
        }
    }

    private fun setupMyLocationOverlay() {
        myLocationOverlay = MyLocationNewOverlay(map)
        myLocationOverlay?.enableMyLocation()
        map.overlays.add(myLocationOverlay)
    }

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST
            )
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            val geoPoint = if (location != null) {
                GeoPoint(location.latitude, location.longitude)
            } else {
                Toast.makeText(this, "Using default location", Toast.LENGTH_SHORT).show()
                DEFAULT_LOCATION
            }

            setMarkerAt(geoPoint)
            mapController.animateTo(geoPoint)
            val address = getAddressFromLatLng(geoPoint.latitude, geoPoint.longitude)
            editAddress.setText(address)
        }.addOnFailureListener {
            Toast.makeText(this, "Location error", Toast.LENGTH_SHORT).show()
            setMarkerAt(DEFAULT_LOCATION)
            editAddress.setText("Default Location: India")
        }
    }

    private fun setMarkerAt(geoPoint: GeoPoint) {
        marker.position = geoPoint
        mapController.setCenter(geoPoint)
        map.invalidate()
    }

    private fun getAddressFromLatLng(lat: Double, lng: Double): String {
        return try {
            val geocoder = Geocoder(this, Locale.getDefault())
            val addresses = geocoder.getFromLocation(lat, lng, 1)
            addresses?.get(0)?.getAddressLine(0) ?: "Lat: $lat, Lng: $lng"
        } catch (e: Exception) {
            "Lat: $lat, Lng: $lng"
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        if (requestCode == LOCATION_PERMISSION_REQUEST &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            setupMyLocationOverlay()
            getCurrentLocation()
        } else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            setMarkerAt(DEFAULT_LOCATION)
            editAddress.setText("Default Location: India")
        }
    }

    override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
        if (p != null) {
            setMarkerAt(p)
            val address = getAddressFromLatLng(p.latitude, p.longitude)
            editAddress.setText(address)
        }
        return true
    }

    override fun longPressHelper(p: GeoPoint?): Boolean {
        return false
    }
}
