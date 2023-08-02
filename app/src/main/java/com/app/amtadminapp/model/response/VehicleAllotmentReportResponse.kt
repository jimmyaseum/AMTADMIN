package com.app.amtadminapp.model.response

import com.google.gson.annotations.SerializedName

data class VehicleAllotmentReportResponse(

    @SerializedName("Result")
    var Result: String? = null,

    @SerializedName("Data")
    var Data: ArrayList<VehicleAllotmentReportModel>? = arrayListOf(),

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

data class VehicleAllotmentReportModel (
    @SerializedName("ID")
    var ID: Int? = null,

    @SerializedName("MobileNo")
    var MobileNo: String? = null,

    @SerializedName("Remarks")
    var Remarks: String? = null,

    @SerializedName("Name")
    var Name: String? = null,

    @SerializedName("BookingNo")
    var BookingNo: String? = null,

    @SerializedName("Gender")
    var Gender: String? = null,

    @SerializedName("Age")
    var Age: Int? = null,

    @SerializedName("HotelVoucherNo")
    var HotelVoucherNo: String? = null,

    @SerializedName("RouteVoucherNo")
    var RouteVoucherNo: String? = null,

    @SerializedName("Message")
    var Message: String? = null,

    @SerializedName("Details")
    var Details: String? = null
)