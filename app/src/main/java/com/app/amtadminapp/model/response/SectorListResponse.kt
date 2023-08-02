package com.app.amtadminapp.model.response

import com.google.gson.annotations.SerializedName

data class SectorListResponse(
    @SerializedName("Data")
    val Data: ArrayList<SectorListModel>? = null,
    @SerializedName("Details")
    val Details: Any?,
    @SerializedName("Message")
    val Message: String,
    @SerializedName("Status")
    val Status: Int
)
