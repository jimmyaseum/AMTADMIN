package com.app.amtadminapp.model.response

import com.google.gson.annotations.SerializedName

data class HotelVoucherResponse(
    @SerializedName("Data")
    val Data: ArrayList<HotelVoucherModel>? = null,
    @SerializedName("Details")
    val Details: Any?,
    @SerializedName("Message")
    val Message: String,
    @SerializedName("Status")
    val Status: Int

)
data class HotelVoucherModel (

    @SerializedName("ID")
    val ID: Int? = null,
    @SerializedName("TourBookingNo")
    val TourBookingNo: String? = null,
    @SerializedName("TourBookingLink")
    val TourbookingLink: String? = null,
    @SerializedName("SectorID")
    val SectorID: Int? = null,
    @SerializedName("SectorName")
    val SectorName: String? = null,
    @SerializedName("CompanyID")
    val CompanyID: Int? = null,
    @SerializedName("CompanyName")
    val CompanyName: String? = null,
    @SerializedName("TourID")
    val TourID: Int? = null,
    @SerializedName("TourName")
    val TourName: String? = null,
    @SerializedName("TourDateCode")
    val TourDateCode: String? = null,
    @SerializedName("TourDate")
    val TourDate: String? = null,
    @SerializedName("NoOfNights")
    val NoOfNights: Int? = null,
    @SerializedName("NoOfDays")
    val NoOfDays: Int? = null,
    @SerializedName("VehicleSharingPaxID")
    val VehicleSharingPaxID: Int? = null,
    @SerializedName("VehicleSharingPax")
    val VehicleSharingPax: String? = null,
    @SerializedName("BookBy")
    val BookByName: String? = null,
    @SerializedName("Branch")
    val Branch: String? = null,
    @SerializedName("Name")
    val Name: String? = null,
    @SerializedName("HotelVoucher")
    val HotelVoucher: String? = null,
    @SerializedName("HotelVoucherLink")
    val HotelVoucherLink: String? = null,
    @SerializedName("TotalAdults")
    val TotalAdults: Int? = null,
    @SerializedName("TotalCWB")
    val TotalCWB: Int? = null,
    @SerializedName("TotalCNB")
    val TotalCNB: Int? = null,
    @SerializedName("TotalInfants")
    val TotalInfants: Int? = null,
    @SerializedName("Total")
    val Total: Int? = null,
    @SerializedName("Remarks")
    val Remarks: String? = null,
    @SerializedName("OldHotelVoucherNo")
    val OldHotelVoucherNo: String? = null,
    @SerializedName("IsCreatedFromApp")
    val IsCreatedFromApp: Boolean? = false

    )





