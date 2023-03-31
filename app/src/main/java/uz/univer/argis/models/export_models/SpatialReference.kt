package uz.univer.argis.models.export_models

import kotlinx.serialization.Serializable


@Serializable
data class SpatialReference(
    val wkid: Int = 4326,
    val latestWkid: Int = 4326,
    val vcsWkid: Int = 5773,
    val latestVcsWkid: Int = 5773
)
