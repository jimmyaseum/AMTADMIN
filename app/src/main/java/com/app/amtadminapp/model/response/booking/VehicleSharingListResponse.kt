package com.app.amtadminapp.model.response.booking

import com.google.gson.annotations.SerializedName

data class VehicleSharingListResponse (
    @SerializedName("Data")
    val Data: ArrayList<VehicleSharingListModel>? = null,
    @SerializedName("Message")
    val Message: String,
    @SerializedName("Status")
    val Status: Int
)

data class VehicleSharingListModel (
    @SerializedName("ID")
    val ID: Int?,
    @SerializedName("Title")
    val Title: String?
)