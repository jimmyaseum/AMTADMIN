package com.app.amtadminapp.model

import com.google.gson.annotations.SerializedName

data class TourBookingPaxResponse (
    @SerializedName("Data")
    val Data: TourBookingPaxModel? = null,
    @SerializedName("Details")
    val Details: Any?,
    @SerializedName("Message")
    val Message: String,
    @SerializedName("Status")
    val Status: Int
)

data class TourBookingPaxModel (
    @SerializedName("Name")
    val Name: String? = null,
    @SerializedName("MobileNo")
    val MobileNo: String? = null
)