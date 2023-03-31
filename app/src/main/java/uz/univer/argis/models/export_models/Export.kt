package uz.univer.argis.models.export_models

import kotlinx.serialization.Serializable

@Serializable
data class Export(
    val displayFieldName: String = "",
    val fieldAliases: FieldAlias = FieldAlias(),
    val geometryType: String = "esriGeometryPolygon",
    val spatialReference: SpatialReference = SpatialReference(),
    val fields: List<Field> = Field.defaultFields,
    val features: List<Feature>
)
