package uz.univer.argis.models.export_models

import kotlinx.serialization.Serializable

@Serializable
data class Field(
    val name: String = "Name",
    val type: String = "esriFieldTypeString",
    val alias: String = "Name",
    val length: Int? = null
) {
    companion object {
        val defaultFields = listOf(
            Field("OID","esriFieldTypeOID","OID"),
            Field("Name","esriFieldTypeString","Name",320),
            Field("FolderPath","esriFieldTypeString","FolderPath",320),
            Field("SymbolID","esriFieldTypeInteger","SymbolID",),
            Field("AltMode","esriFieldTypeSmallInteger","AltMode",),
            Field("Base","esriFieldTypeDouble","Base",),
            Field("Clamped","esriFieldTypeSmallInteger","Clamped",),
            Field("Extruded","esriFieldTypeSmallInteger","Extruded",),
            Field("Snippet","esriFieldTypeString","Snippet",268435455),
            Field("PopupInfo","esriFieldTypeString","PopupInfo",268435455),
//    Field("Shape_Length","esriFieldTypeDouble","Shape_Length",),
//            Field("Shape_Area","esriFieldTypeDouble","Shape_Area",),
        )

    }
}
