package com.app.amtadminapp.model.response

import com.google.gson.annotations.SerializedName

data class HotelVoucherListResponse(

    @SerializedName("Data")
    val Data: ArrayList<HotelVoucherListModel>? = null,
    @SerializedName("Details")
    val Details: Any?,
    @SerializedName("Message")
    val Message: String,
    @SerializedName("Status")
    val Status: Int
)

data class HotelVoucherListModel (
    @SerializedName("HotelVoucher")
    val HotelVoucher: String? = null,
    @SerializedName("HotelVoucherLink")
    val HotelVoucherLink: String? = null,
    @SerializedName("TourBookingNo")
    val TourBookingNo: String? = null,
    @SerializedName("TourbookingLink")
    val TourbookingLink: String? = null,
    @SerializedName("TourName")
    val TourName: String? = null,
    @SerializedName("TourDate")
    val TourDate: String? = null,
    @SerializedName("CompanyName")
    val CompanyName: String? = null,
    @SerializedName("NoOfNights")
    val NoOfNights: Int? = null,
    @SerializedName("VehicleSharingPax")
    val VehicleSharingPax: String? = null,
    @SerializedName("BookBy")
    val BookBy: String? = null,
    @SerializedName("Branch")
    val Branch: String? = null

)



