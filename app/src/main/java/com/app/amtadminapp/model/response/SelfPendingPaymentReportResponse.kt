package com.app.amtadminapp.model.response

import com.google.gson.annotations.SerializedName

data class SelfPendingPaymentReportResponse(

    @SerializedName("Result")
    var Result: String? = null,

    @SerializedName("Data")
    var Data: ArrayList<SelfPendingPaymentReportModel>? = arrayListOf(),

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

data class SelfPendingPaymentReportModel (

    @SerializedName("TourBookingNo")
    var TourBookingNo: String? = null,

    @SerializedName("Name")
    var Name: String? = null,

    @SerializedName("MobileNo")
    var MobileNo: String? = null,

    @SerializedName("TourCode")
    var TourCode: String? = null,

    @SerializedName("StartDate")
    var StartDate: String? = null,

    @SerializedName("RemainAmount")
    var RemainAmount: String? = null,

    @SerializedName("BookBy")
    var BookBy: String? = null,

    @SerializedName("Message")
    var Message: String? = null,

    @SerializedName("Details")
    var Details: String? = null
)