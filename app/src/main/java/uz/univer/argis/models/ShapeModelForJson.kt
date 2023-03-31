package uz.univer.argis.models

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import kotlinx.serialization.Serializable

@kotlinx.serialization.Serializable
data class ShapeModelForJsonList(
    val shapeModelJsonFormats: List<ShapeModelJsonFormat>
)

@kotlinx.serialization.Serializable
data class ShapeModelJsonFormat(
    val points: List<Point>,
    val color: Int,
    val name: String
)

@Serializable
data class Point(
    val lat: Double,
    val lng: Double,
)

fun Marker.toPoint(): Point = Point(position.latitude, position.longitude)
fun Point.toLatLng() = LatLng(lat, lng)
fun ShapeModel.toJsonFormat() = ShapeModelJsonFormat(
    points = markers.map { it.toPoint() },
    color = type.color,
    name = type.name
)



