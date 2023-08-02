package com.app.amtadminapp.model.response

import com.google.gson.annotations.SerializedName

data class RouteVoucherListResponse(

    @SerializedName("Data")
    val Data: ArrayList<RouteVoucherListModel>? = null,
    @SerializedName("Details")
    val Details: Any?,
    @SerializedName("Message")
    val Message: String,
    @SerializedName("Status")
    val Status: Int
)

data class RouteVoucherListModel (
    @SerializedName("RouteVoucher")
    val RouteVoucher: String? = null,
    @SerializedName("RouteVoucherLink")
    val RouteVoucherLink: String? = null,
    @SerializedName("OldRouteVoucherNo")
    val OldRouteVoucherNo: String? = null,
    @SerializedName("Sector")
    val Sector: String? = null,
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
    @SerializedName("TourBookingNo")
    val TourBookingNo: String? = null,
    @SerializedName("TourbookingLink")
    val TourbookingLink: String? = null
)


