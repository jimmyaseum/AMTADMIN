package com.app.amtadminapp.model.response

import com.google.gson.annotations.SerializedName

data class BranchWiseTourBookingSummaryReportResponse(

    @SerializedName("Result")
    var Result: String? = null,

    @SerializedName("Data")
    var Data: ArrayList<BranchWiseTourBookingSummaryReportModel>? = arrayListOf(),

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

data class BranchWiseTourBookingSummaryReportModel (

    @SerializedName("BranchName")
    var BranchName: String? = null,

    @SerializedName("TotalBooking")
    var TotalBooking: Int? = null,

    @SerializedName("TotalPax")
    var TotalPax: Int? = null,

    @SerializedName("ConfirmBooking")
    var ConfirmBooking: Int? = null,

    @SerializedName("ConfirmPax")
    var ConfirmPax: Int? = null,

    @SerializedName("CancellBooking")
    var CancellBooking: Int? = null,

    @SerializedName("CancellPax")
    var CancellPax: Int? = null,

    @SerializedName("Message")
    var Message: String? = null,

    @SerializedName("Details")
    var Details: String? = null
)