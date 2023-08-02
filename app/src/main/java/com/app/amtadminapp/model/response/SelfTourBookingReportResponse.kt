package com.app.amtadminapp.model.response

import com.google.gson.annotations.SerializedName

data class SelfTourBookingReportResponse(

    @SerializedName("Result")
    var Result: String? = null,

    @SerializedName("Data")
    var Data: ArrayList<SelfTourBookingReportModel>? = arrayListOf(),

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

data class SelfTourBookingReportModel (

    @SerializedName("ID")
    var ID: Int? = null,

    @SerializedName("TourBookingNo")
    var TourBookingNo: String? = null,

    @SerializedName("TourName")
    var TourName: String? = null,

    @SerializedName("CustomerName")
    var CustomerName: String? = null,

    @SerializedName("MobileNo")
    var MobileNo: String? = null,

    @SerializedName("TourStartDate")
    var TourStartDate: String? = null,

    @SerializedName("NoOfNights")
    var NoOfNights: String? = null,

    @SerializedName("BookBy")
    var BookBy: String? = null,

    @SerializedName("TotalPax")
    var TotalPax: String? = null,

    @SerializedName("TourBookingStatus")
    var TourBookingStatus: String? = null,

    @SerializedName("Message")
    var Message: String? = null,

    @SerializedName("Details")
    var Details: String? = null
)