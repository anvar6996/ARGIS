package uz.univer.argis.domain

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.content.res.Resources
import android.graphics.Color
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.google.maps.android.data.geojson.GeoJsonFeature
import com.google.maps.android.data.geojson.GeoJsonLayer
import com.google.maps.android.data.geojson.GeoJsonPoint
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.json.JSONObject
import uz.univer.argis.models.*
import uz.univer.argis.models.export_models.Attributes
import uz.univer.argis.models.export_models.Export
import uz.univer.argis.models.export_models.Feature
import uz.univer.argis.models.export_models.Geometry

class MapShapeDrawerManager(
  private val resources: Resources,
  private val sharedPreferences: SharedPreferences,
  private val showMessage: (message: String) -> Unit = {}
) : OnMapReadyCallback {
  private lateinit var map: GoogleMap
  var hasPermission: Boolean = false

  val shapeModels = mutableListOf<ShapeModel>()
  private val markers = mutableListOf<Marker>()
  private var isDragging = false
  val types = yerTurlari
  val typesName = types.map { it.name }
  var selectedType = types.first()
    set(value) {
      field = value
      drawCurrentMarkersPolygon()
    }

  override fun onMapReady(googleMap: GoogleMap) {
    map = googleMap
    map.mapType = GoogleMap.MAP_TYPE_HYBRID
    addMarkerOnClickedPositionListener()
    addMarkerDragListener()
    updateMapUI()
    loadFromCache()
  }

  @SuppressLint("MissingPermission")
  fun updateMapUI() {
    map.isMyLocationEnabled = hasPermission
    map.uiSettings.isMyLocationButtonEnabled = hasPermission
  }

  private fun addMarkerOnClickedPositionListener() {
    map.setOnMapClickListener {
      addMarker(it)
    }
  }

  private fun addMarkerDragListener() {
    map.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
      override fun onMarkerDrag(p0: Marker) {
        drawCurrentMarkersPolygon()
      }

      override fun onMarkerDragEnd(p0: Marker) {
        isDragging = false
        drawCurrentMarkersPolygon()
      }

      override fun onMarkerDragStart(p0: Marker) {
        isDragging = true
        drawCurrentMarkersPolygon()
      }

    })
  }

  private var currentPolygon: Polygon? = null
  fun drawCurrentMarkersPolygon() {
    currentPolygon?.remove()
    if (markers.isEmpty()) return

    val polygonOptions = PolygonOptions()
    polygonOptions.addAll(markers.map { it.position })
    map.addPolygon(polygonOptions).apply {
      currentPolygon = this
      if (!isDragging) fillColor = resources.getColor(selectedType.color)
      strokeColor = Color.BLUE
      strokeWidth = 5f
    }
  }

  private fun addMarker(latLng: LatLng) {
    val markerOptions = MarkerOptions()
    markerOptions.position(latLng)
    map.addMarker(markerOptions)?.apply {
      isDraggable = true
      markers.add(this)
    }
    drawCurrentMarkersPolygon()
  }

  fun removeLastMarker() {
    when {
      markers.isNotEmpty() -> {
        markers.removeLast().apply {
          remove()
          drawCurrentMarkersPolygon()
        }
      }
      shapeModels.isNotEmpty() && markers.isEmpty() -> {
        val lastShapeModel = shapeModels.removeLast()
        currentPolygon = lastShapeModel.polygon
        selectedType = types.firstOrNull { it == lastShapeModel.type } ?: types.first()
        markers.addAll(lastShapeModel.markers)
        markers.forEach { it.toEditableState() }
        currentPolygon?.toEditableState()

        drawCurrentMarkersPolygon()
      }
      else -> {
        showMessage("Nuqtalar yo'q")
      }
    }

  }

  fun saveCurrentShape() {
    if (markers.isNotEmpty() && currentPolygon != null) {
      markers.forEach { it.toSavedState() }
      currentPolygon!!.toSavedState()
      shapeModels.add(
        ShapeModel(
          markers = markers.toList(), polygon = currentPolygon!!, selectedType
        )
      )

      markers.clear()
      currentPolygon = null
      showMessage("Joriy shakl saqlandi, keyingi shaklni chizishingiz mumkin")
    } else {
      showMessage("Avval biron shaklni chizing")
    }
  }

  fun clearAllShapes() {
    shapeModels.forEach { shapeModel ->
      shapeModel.polygon.remove()
      shapeModel.markers.forEach { marker -> marker.remove() }
    }
    shapeModels.clear()

    currentPolygon?.remove()
    markers.forEach { marker -> marker.remove() }
    markers.clear()

    sharedPreferences.edit().clear().apply()

    showMessage("Tozalandi")
  }


  private fun Marker.toSavedState() {
    isDraggable = false
    isVisible = false
  }

  private fun Marker.toEditableState() {
    isDraggable = true
    isVisible = true
    setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
  }

  private fun Polygon.toSavedState() {
    strokeColor = Color.GRAY
  }

  private fun Polygon.toEditableState() {
    strokeColor = Color.BLUE
  }

  fun loadToCache() {
    val cache = ShapeModelForJsonList(shapeModelJsonFormats = shapeModels.map { it.toJsonFormat() })
    val json = Json.encodeToString(cache)
    sharedPreferences.edit().putString(json_key, json).apply()
  }

  fun loadFromCache() {
    val defaultJson = """{
            |"shapeModelJsonFormats":[]
            |}""".trimMargin()
    val json = sharedPreferences.getString(json_key, defaultJson) ?: return
    val data = Json.decodeFromString<ShapeModelForJsonList>(json)
    shapeModels.clear()
    data.shapeModelJsonFormats.forEach { shapeModelJsonFormat ->
      val markers = addMarkers(shapeModelJsonFormat.points)
      val polygon = addPolygon(
        shapeModelJsonFormat.points.map { it.toLatLng() }, shapeModelJsonFormat.color
      )
      shapeModels.add(
        ShapeModel(
          markers, polygon, Type(shapeModelJsonFormat.name, shapeModelJsonFormat.color)
        )
      )
    }
  }

  private fun addMarkers(points: List<Point>): List<Marker> {
    val markers = mutableListOf<Marker>()
    points.forEach {
      val marker = addMarker(it)
      markers.add(marker)
    }
    return markers
  }

  private fun addMarker(point: Point): Marker {
    var marker: Marker? = null
    val markerOptions = MarkerOptions()
    markerOptions.position(point.toLatLng())
    while (marker == null) {
      marker = map.addMarker(markerOptions)
    }
    marker.toSavedState()
    return marker
  }

  private fun addPolygon(latLngs: List<LatLng>, color: Int): Polygon {
    val polygonOptions = PolygonOptions()
    polygonOptions.addAll(latLngs)
    return map.addPolygon(polygonOptions).apply {
      toSavedState()
      strokeWidth = 5f
      fillColor = resources.getColor(color)
    }
  }

  val json = Json { encodeDefaults = true }
  fun export(): String {

    val geoJsonData = JSONObject()
    val layer = GeoJsonLayer(map, geoJsonData)
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

    val features = mutableListOf<Feature>()
    val geoFutures = mutableListOf<GeoJsonFeature>()
    val points = mutableListOf<GeoJsonPoint>()
    shapeModels.forEachIndexed { index, shapeModel ->
      val coordinates = mutableListOf<List<Double>>()
      shapeModel.markers.forEach { marker ->

        val t = listOf(marker.position.latitude, marker.position.longitude)
        coordinates.add(t)

      }
      features.add(
        Feature(
          Attributes(OID = index + 1), Geometry(
            listOf(
              coordinates
            )
          )
        )
      )
    }

    val export = Export(
      features = features
    )


    return layer.toString()
//    geoJsonData.
//    return json.encodeToString(export)
  }

  companion object {
    const val json_key = "GeoJSON"
  }
}