package uz.univer.argis.domain

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.postDelayed
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.data.geojson.GeoJsonFeature
import com.google.maps.android.data.geojson.GeoJsonLayer
import com.google.maps.android.data.geojson.GeoJsonPoint
import org.json.JSONObject
import uz.univer.argis.R
import uz.univer.argis.databinding.ActivityMapsBinding
import java.io.FileOutputStream
import java.io.IOException


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
  private lateinit var binding: ActivityMapsBinding
  private val requestPermission =
    registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
      mapShapeDrawerManager.hasPermission = isGranted
      mapShapeDrawerManager.updateMapUI()
    }
  private lateinit var googleMap: GoogleMap


  private val sharedPreferences by lazy {
    PreferenceManager.getDefaultSharedPreferences(applicationContext)
  }

  private val mapShapeDrawerManager by lazy {
    MapShapeDrawerManager(resources, sharedPreferences) {
      Toast.makeText(this, it, Toast.LENGTH_LONG).show()
    }
  }

  override fun onMapReady(p0: GoogleMap) {
    googleMap = p0
    val geoJsonData: JSONObject? = null
    val layer = GeoJsonLayer(googleMap, geoJsonData)
    val point1 = GeoJsonPoint(LatLng(41.311081, 69.240562))
    val point2 = GeoJsonPoint(LatLng(42.311081, 67.240562))
    val point3 = GeoJsonPoint(LatLng(43.311081, 67.240562))
    val properties = hashMapOf("Ocean" to "South Atlantic")
    val pointFeature1 = GeoJsonFeature(point1, "Origin", properties, null)
    val pointFeature2 = GeoJsonFeature(point2, "Origin", properties, null)
    val pointFeature3 = GeoJsonFeature(point3, "Origin", properties, null)
    layer.addFeature(pointFeature1)
    layer.addFeature(pointFeature2)
    layer.addFeature(pointFeature3)

  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = ActivityMapsBinding.inflate(layoutInflater)
    setContentView(binding.root)

    val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
    mapFragment.getMapAsync(mapShapeDrawerManager)

    initDropDownView()
    initClicks()

  }

  override fun onResume() {
    super.onResume()
    binding.root.postDelayed(1000) {
      initCurrentLocation()
    }
  }

  @SuppressLint("MissingPermission")
  private fun initCurrentLocation() {
    if (ContextCompat.checkSelfPermission(
        this.applicationContext, android.Manifest.permission.ACCESS_FINE_LOCATION
      ) == PackageManager.PERMISSION_GRANTED
    ) {
      mapShapeDrawerManager.hasPermission = true
      mapShapeDrawerManager.updateMapUI()
      Log.d("RTR", "initCurrentLocation: hasPermission=true")
    } else {
      mapShapeDrawerManager.hasPermission = false
      Log.d("RTR", "initCurrentLocation: hasPermission=false")
      requestPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }
  }

  private fun initDropDownView() {
    binding.type.setText(mapShapeDrawerManager.selectedType.name)
    binding.type.setAdapter(
      ArrayAdapter(
        this, R.layout.item_drop_down, mapShapeDrawerManager.typesName.toTypedArray()
      )
    )
  }

  private fun initClicks() {
    binding.type.setOnItemClickListener { adapterView, view, pos, id ->
      mapShapeDrawerManager.selectedType = mapShapeDrawerManager.types[pos]
    }
    binding.undo.setOnClickListener {
      mapShapeDrawerManager.removeLastMarker()
    }
    binding.clear.setOnClickListener {
      mapShapeDrawerManager.clearAllShapes()
    }
    binding.done.setOnClickListener {
      mapShapeDrawerManager.saveCurrentShape()
    }
    binding.export.setOnClickListener {
      if (mapShapeDrawerManager.shapeModels.isNotEmpty()) {
        val intent = Intent().apply {
          action = Intent.ACTION_CREATE_DOCUMENT
          type = "text/*"
          putExtra(Intent.EXTRA_TITLE, "back_up.GeoJSON")
        }
        startActivityForResult(intent, CREATE_FILE)
      } else {
        Toast.makeText(this, "Ma'lumotlar yo'q", Toast.LENGTH_SHORT).show()
      }
    }
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (requestCode == CREATE_FILE && resultCode == Activity.RESULT_OK) {
      data?.data?.let {
        writeTextToUri(it, mapShapeDrawerManager.export())
      }
    }
  }

  override fun onPause() {
    mapShapeDrawerManager.loadToCache()
    super.onPause()
  }

  @Throws(IOException::class)
  private fun writeTextToUri(uri: Uri, text: String) {
    contentResolver.openFileDescriptor(uri, "w")?.use {
      FileOutputStream(it.fileDescriptor).use {
        it.write(text.toByteArray())
      }
    }
  }

  private fun hasPermission(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(
      this, permission
    ) == PackageManager.PERMISSION_GRANTED
  }

  companion object {
    const val CREATE_FILE = 111
    const val OPEN_FILE = 222
    const val TAG = "SettingFragment"
  }
}
