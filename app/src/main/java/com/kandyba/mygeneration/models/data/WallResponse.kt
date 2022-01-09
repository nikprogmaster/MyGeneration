package com.kandyba.mygeneration.models.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class WallResponse(
    @SerializedName("response")
    @Expose
    var response: Response
)

data class Response(
    @SerializedName("count")
    @Expose
    var count: Int? = null,

    @SerializedName("items")
    @Expose
    var items: List<PostModel>? = null
)