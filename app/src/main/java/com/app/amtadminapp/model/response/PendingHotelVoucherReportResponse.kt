package com.app.amtadminapp.model.response

import com.google.gson.annotations.SerializedName

data class PendingHotelVoucherReportResponse(

    @SerializedName("Result")
    var Result: String? = null,

    @SerializedName("Data")
    var Data: ArrayList<PendingHotelVoucherReportModel>? = arrayListOf(),

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

data class PendingHotelVoucherReportModel (
    @SerializedName("PBN")
    var PBN: String? = null,

    @SerializedName("TourID")
    var TourID: Int? = null,

    @SerializedName("Name")
    var Name: String? = null,

    @SerializedName("TourName")
    var TourName: String? = null,

    @SerializedName("TourDateCode")
    var TourDateCode: String? = null,

    @SerializedName("NoOfNights")
    var NoOfNights: Int? = null,

    @SerializedName("TourStartDate")
    var TourStartDate: String? = null,

    @SerializedName("TourEndDate")
    var TourEndDate: String? = null,

    @SerializedName("CreatedOn")
    var CreatedOn: String? = null,

    @SerializedName("Message")
    var Message: String? = null,

    @SerializedName("Details")
    var Details: String? = null
)