package com.app.amtadminapp.model.response

import com.google.gson.annotations.SerializedName

data class ConfirmedHotelVoucherReportResponse(

    @SerializedName("Result")
    var Result: String? = null,

    @SerializedName("Data")
    var Data: ArrayList<ConfirmedHotelVoucherReportModel>? = arrayListOf(),

    @SerializedName("Details")
    var Details: String? = null,

    @SerializedName("Message")
    var Message: String? = null,

    @SerializedName("Status")
    var Status: Int? = null,

    @SerializedName("CachingStatus")
    var CachingStatus: String? = null,

    @SerializedName("ItemCount")
    var ItemCount: Int? = null,

    @SerializedName("TotalPages")
    var TotalPages: String? = null
)

data class ConfirmedHotelVoucherReportModel (
    @SerializedName("CheckInDate")
    var CheckInDate: String? = null,

    @SerializedName("CheckOutDate")
    var CheckOutDate: String? = null,

    @SerializedName("TourDateCode")
    var TourDateCode: String? = null,

    @SerializedName("HotelName")
    var HotelName: String? = null,

    @SerializedName("CityName")
    var CityName: String? = null,

    @SerializedName("TotalPax")
    var TotalPax: Int? = null,

    @SerializedName("ExtraBed")
    var ExtraBed: Int? = null,

    @SerializedName("TotalRooms")
    var TotalRooms: Int? = null,

    @SerializedName("NoOfNights")
    var NoOfNights: String? = null,

    @SerializedName("Message")
    var Message: String? = null,

    @SerializedName("Details")
    var Details: String? = null
)