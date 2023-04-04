package uz.univer.argis.models.export_data

import com.google.maps.android.data.geojson.GeoJsonFeature
import com.google.maps.android.data.geojson.GeoJsonPoint

data class GeoJsonData(
  val geoFeatures: GeoJsonFeature, val geoPoints: ArrayList<GeoJsonPoint>

)