package com.app.amtadminapp.model.response

import com.google.gson.annotations.SerializedName

data class TourBookingListResponse (
    @SerializedName("Data")
    val Data: ArrayList<TourBookingListModel>? = null,
    @SerializedName("Details")
    val Details: Any?,
    @SerializedName("Message")
    val Message: String,
    @SerializedName("Status")
    val Status: Int
        )

data class TourBookingListModel (
    @SerializedName("ID")
    val ID: Int? = null,
    @SerializedName("CustomerID")
    val CustomerID: Int? = null,
    @SerializedName("TourBookingNo")
    val TourBookingNo: String? = null,
    @SerializedName("TourbookingLink")
    val TourbookingLink: String? = null,
    @SerializedName("Name")
    val Name: String? = null,
    @SerializedName("MobileNo")
    val MobileNo: String? = null,
    @SerializedName("Sector")
    val Sector: String? = null,
    @SerializedName("TravelType")
    val TravelType: String? = null,
    @SerializedName("TourName")
    val Tour: String? = null,
    @SerializedName("TourDateCode")
    val TourDateCode: String? = null,
    @SerializedName("NoOfNights")
    val NoOfNights: Int? = null,
    @SerializedName("GroupBookingNo")
    val GroupBookingNo: String? = null,
    @SerializedName("TourBookingStatus")
    val TourBookingStatus: String? = null,
    @SerializedName("BookBy")
    val BookBy: String? = null,
    @SerializedName("Branch")
    val Branch: String? = null

    )



