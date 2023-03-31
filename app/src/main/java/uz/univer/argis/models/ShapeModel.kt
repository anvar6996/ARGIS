package uz.univer.argis.models

import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.Polygon

data class ShapeModel(
    val markers: List<Marker>,
    var polygon: Polygon,
    val type: Type
)
