package com.app.amtadminapp.model.response

import com.google.gson.annotations.SerializedName

data class DashBoardResponse (
    @SerializedName("Data")
    val Data: DashBoardModel? = null,
    @SerializedName("Details")
    val Details: Any?,
    @SerializedName("Message")
    val Message: String,
    @SerializedName("Status")
    val Status: Int
)
data class DashBoardModel (
    @SerializedName("PendingPayment")
    val PendingPayment: Int?,
    @SerializedName("SectorList")
    val SectorList: ArrayList<SectorModel>,
    @SerializedName("TotalAirlineVoucher")
    val TotalAirlineVoucher: Int?,
    @SerializedName("TotalBooking")
    val TotalBooking: Int?,
    @SerializedName("TotalDeparture")
    val TotalDeparture: Int?,
    @SerializedName("TotalHotelVoucher")
    val TotalHotelVoucher: Int?,
    @SerializedName("TotalPayment")
    val TotalPayment: Int?,
    @SerializedName("TotalPendingForms")
    val TotalPendingForms: Int?,
    @SerializedName("TotalRouteVoucher")
    val TotalRouteVoucher: Int?
)

data class SectorModel (
    @SerializedName("SectorName")
    val SectorName: String?,
    @SerializedName("Sectorid")
    val Sectorid : Int?,
    @SerializedName("TotalTour")
    val TotalTour: Int?
    )