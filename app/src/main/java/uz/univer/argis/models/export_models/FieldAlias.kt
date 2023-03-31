package uz.univer.argis.models.export_models

import kotlinx.serialization.Serializable

@Serializable
data class FieldAlias(
    val OID: String =  "OID",
    val Name: String =  "Name",
    val FolderPath: String =  "FolderPath",
    val SymbolID: String =  "SymbolID",
    val AltMode: String =  "AltMode",
    val Base: String =  "Base",
    val Clamped: String =  "Clamped",
    val Extruded: String =  "Extruded",
    val Snippet: String =  "Snippet",
    val PopupInfo: String =  "PopupInfo",
    val Shape_Length: String =  "Shape_Length",
    val Shape_Area: String =  "Shape_Area"
)
