package com.app.amtadminapp.model.response

import com.google.gson.annotations.SerializedName

data class HotelVoucherFindByIDResponse(
    @SerializedName("Data")
    val Data: HotelVoucherFindByIDModel? = null,
    @SerializedName("Details")
    val Details: Any?,
    @SerializedName("Message")
    val Message: String,
    @SerializedName("Status")
    val Status: Int
)

data class HotelVoucherFindByIDModel(
    @SerializedName("ID")
    val ID: Int? = null,
    @SerializedName("TourBookingNo")
    val TourBookingNo: String? = null,
    @SerializedName("placeList")
    val placeList: ArrayList<placeList>? = ArrayList()
)
data class placeList (
    @SerializedName("ID")
    val ID: Int? = null,
    @SerializedName("HotelBookingID")
    val HotelBookingID: String? = null,
    @SerializedName("CityID")
    val CityID: String? = null,
    @SerializedName("CityName")
    val CityName: String? = null,
    @SerializedName("Document")
    val Document: String? = null,
    @SerializedName("HotelVoucherImage")
    val HotelVoucherImage: String? = null
)