package com.app.amtadminapp.model.response

import com.google.gson.annotations.SerializedName

data class RouteVoucherResponse(
    @SerializedName("Data")
    val Data: ArrayList<RouteVoucherModel>? = null,
    @SerializedName("Details")
    val Details: Any?,
    @SerializedName("Message")
    val Message: String,
    @SerializedName("Status")
    val Status: Int

)
data class RouteVoucherModel (

    @SerializedName("ID")
    val ID: Int? = null,
    @SerializedName("RouteVoucher")
    val RouteVoucher: String? = null,
    @SerializedName("RouteVoucherLink")
    val RouteVoucherLink: String? = null,
    @SerializedName("TourBookingNo")
    val TourBookingNo: String? = null,
    @SerializedName("TourBookingLink")
    val TourBookingLink: String? = null,
    @SerializedName("SectorID")
    val SectorID: Int? = null,
    @SerializedName("Sector")
    val Sector: String? = null,
    @SerializedName("TourID")
    val TourID: Int? = null,
    @SerializedName("Tour")
    val Tour: String? = null,
    @SerializedName("TourDateCode")
    val TourDateCode: String? = null,
    @SerializedName("VehicleType")
    val VehicleType: String? = null,
    @SerializedName("BookBy")
    val BookBy: String? = null,
    @SerializedName("Branch")
    val Branch: String? = null,
    @SerializedName("Name")
    val Name: String? = null
)

