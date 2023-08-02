package com.app.amtadminapp.model.response

import com.google.gson.annotations.SerializedName

data class TourPackageListResponse(
    @SerializedName("Data")
    val Data: ArrayList<TourPackageListModel>? = null,
    @SerializedName("Details")
    val Details: Any?,
    @SerializedName("Message")
    val Message: String,
    @SerializedName("Status")
    val Status: Int
)
data class TourPackageListModel (
    @SerializedName("ID")
    val ID: Int?,
    @SerializedName("SectorID")
    val SectorID: Int?,
    @SerializedName("TourID")
    val TourID: Int?,
    @SerializedName("TravelType")
    val TravelType: String?,
    @SerializedName("TourCode")
    val TourCode: String?,
    @SerializedName("TourName")
    val TourName: String?
)