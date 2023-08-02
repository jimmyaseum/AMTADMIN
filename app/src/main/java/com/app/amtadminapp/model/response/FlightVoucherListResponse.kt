package com.app.amtadminapp.model.response

import com.google.gson.annotations.SerializedName

data class FlightVoucherListResponse(

    @SerializedName("Data")
    val Data: ArrayList<FlightVoucherListModel>? = null,
    @SerializedName("Details")
    val Details: Any?,
    @SerializedName("Message")
    val Message: String,
    @SerializedName("Status")
    val Status: Int
)

data class FlightVoucherListModel (
    @SerializedName("AirlineVoucherNo")
    val AirlineVoucherNo: String? = null,
    @SerializedName("OldAirlineVoucherNo")
    val OldAirlineVoucherNo: String? = null,
    @SerializedName("TourBookingNo")
    val TourBookingNo: String? = null,
    @SerializedName("TourbookingLink")
    val TourbookingLink: String? = null,
    @SerializedName("CompanyName")
    val CompanyName: String? = null,
    @SerializedName("TicketPurchasedDate")
    val TicketPurchasedDate: String? = null,
    @SerializedName("Journey")
    val Journey: String? = null,
    @SerializedName("NoOfPax")
    val NoOfPax: Int? = null,
    @SerializedName("TotalPrice")
    val TotalPrice: Int? = null,
    @SerializedName("BookBy")
    val BookBy: String? = null,
    @SerializedName("Branch")
    val Branch: String? = null
)
