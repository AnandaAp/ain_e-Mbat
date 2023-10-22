package models

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val status: String = "",
    val type: String = "",
    val version: String = ""
)