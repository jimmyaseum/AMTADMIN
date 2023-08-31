package com.app.amtadminapp.model.response

import com.google.gson.annotations.SerializedName

data class AirlineVoucherResponse(
    @SerializedName("Data")
    val Data: ArrayList<AirlineVoucherModel>? = null,
    @SerializedName("Details")
    val Details: Any?,
    @SerializedName("Message")
    val Message: String,
    @SerializedName("Status")
    val Status: Int
)

data class AirlineVoucherModel (
    @SerializedName("ID")
    val ID: Int? = null,
    @SerializedName("AirlineVoucherNo")
    val AirlineVoucherNo: String? = null,
    @SerializedName("AirlineVoucherLink")
    val AirlineVoucherLink: String? = null,
    @SerializedName("TourBookingNo")
    val TourBookingNo: String? = null,
    @SerializedName("TourBookingLink")
    val TourBookingLink: String? = null,
    @SerializedName("CompanyID")
    val CompanyID: Int? = null,
    @SerializedName("CompanyName")
    val CompanyName: String? = null,
    @SerializedName("Journey")
    val Journey: String? = null,
    @SerializedName("Name")
    val Name: String? = null,
    @SerializedName("NoOfPax")
    val NoOfPax: Int? = null,
    @SerializedName("PNR")
    val PNR: String? = null,
    @SerializedName("Status")
    val Status: String? = null,
    @SerializedName("TicketPurchasedDate")
    val TicketPurchasedDate: String? = null,
    @SerializedName("TotalPrice")
    val TotalPrice: Double? = null,
    @SerializedName("BookBy")
    val BookBy: String? = null,
    @SerializedName("Branch")
    val Branch: String? = null,
    @SerializedName("IsCreatedFromApp")
    val IsCreatedFromApp: Boolean? = false,
    @SerializedName("AirlineVoucherTicket")
    val AirlineVoucherTicket: String? = null,
    @SerializedName("DeparturePNRNo")
    val DeparturePNRNo: String? = null,
    @SerializedName("ArrivalPNRNo")
    val ArrivalPNRNo: String? = null

)

