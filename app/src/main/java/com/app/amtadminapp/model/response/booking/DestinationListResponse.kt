package com.app.amtadminapp.model.response.booking

import com.google.gson.annotations.SerializedName

data class DestinationListResponse (
    @SerializedName("Data")
    val Data: ArrayList<DestinationListModel>? = null,
    @SerializedName("Message")
    val Message: String,
    @SerializedName("Status")
    val Status: Int
)

data class DestinationListModel (
    @SerializedName("ID")
    val ID: Int?,
    @SerializedName("RegionID")
    val RegionID: Int?,
    @SerializedName("Region")
    val Region: String?,
    @SerializedName("SectorType")
    val SectorType: String?,
    @SerializedName("SectorName")
    val SectorName: String?,
    @SerializedName("Prefix")
    val Prefix: String?,
    @SerializedName("DestinationImage")
    val DestinationImage: String?,
    @SerializedName("ShortDescription")
    val ShortDescription: String?,
    @SerializedName("LongDescription")
    val LongDescription: String?,
    @SerializedName("BestTimeToVisit")
    val BestTimeToVisit: String?,
    @SerializedName("DestinationURL")
    val DestinationURL: String?,
    @SerializedName("IsTopDestination")
    val IsTopDestination: Boolean? = false,
    @SerializedName("IsTrendingDestination")
    val IsTrendingDestination: Boolean? = false,
    @SerializedName("IsWeekendGateway")
    val IsWeekendGateway: Boolean? = false,
    @SerializedName("IsShowOnMenu")
    val IsShowOnMenu: Boolean? = false,
    @SerializedName("City")
    val City: ArrayList<CityModel1>? = null
)

data class CityModel1 (
    @SerializedName("SectorID")
    val SectorID: Int?,
    @SerializedName("CityID")
    val CityID: Int?,
    @SerializedName("City")
    val City: String?
)
