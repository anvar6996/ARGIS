package uz.univer.argis.models.export_models

import kotlinx.serialization.Serializable

@kotlinx.serialization.Serializable
data class Feature(
    val attributes: Attributes = Attributes(),
    val geometry: Geometry
)
@kotlinx.serialization.Serializable
data class Attributes(
    val OID: Int = 1,
    val Name: String? = "Шудгор",
    val FolderPath: String? = "Шудгор.kmz",
    val SymbolID: Int = 0,
    val AltMode: Int? = null,
    val Base: Int? = null,
    val Clamped: Int? = null,
    val Extruded: Int? = null,
    val Snippet: String = "",
    val PopupInfo: String ="",
    //val Shape_Length: Double = 0.036962292940364579,
    //val Shape_Area: Double = 8.5870848037668601e-05
)
@Serializable
data class Geometry(
    val rings: List<List<List<Double>>>
)
