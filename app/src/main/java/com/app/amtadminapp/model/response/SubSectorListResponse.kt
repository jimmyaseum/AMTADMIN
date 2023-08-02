package com.app.amtadminapp.model.response

import com.google.gson.annotations.SerializedName

data class SubSectorListResponse(
    @SerializedName("Data")
    val Data: SubSectorList? = null,
    @SerializedName("Details")
    val Details: Any?,
    @SerializedName("Message")
    val Message: String,
    @SerializedName("Status")
    val Status: Int
)
data class SubSectorList (
    @SerializedName("SectorType")
    val SectorType: String?,
    @SerializedName("SubSectorList")
    val SubSectorList: ArrayList<SubSectorListModel>? = null
)
data class SubSectorListModel (
    @SerializedName("ID")
    val ID: Int?,
    @SerializedName("RegionID")
    val RegionID: Int?,
    @SerializedName("DestinationID")
    val DestinationID: Int?,
    @SerializedName("Region")
    val Region: String?,
    @SerializedName("Destination")
    val Destination: String?,
    @SerializedName("SubSectorName")
    val SubSectorName: String?
)
