package com.kandyba.mygeneration.models.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WallResponse(
    @SerialName("response")
    var response: Response
)

@Serializable
data class Response(
    @SerialName("count")
    var count: Int? = null,

    @SerialName("items")
    var items: List<PostModel>? = null
)