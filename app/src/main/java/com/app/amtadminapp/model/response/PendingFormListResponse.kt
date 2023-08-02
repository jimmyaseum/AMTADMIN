package com.app.amtadminapp.model.response

import com.google.gson.annotations.SerializedName

data class PendingFormListResponse (
        @SerializedName("Data")
        val Data: ArrayList<PendingFormListModel>? = null,
        @SerializedName("Details")
        val Details: Any?,
        @SerializedName("Message")
        val Message: String,
        @SerializedName("Status")
        val Status: Int
)

data class PendingFormListModel (
        @SerializedName("TourBookingNo")
        val TourBookingNo: String? = null,
        @SerializedName("TourbookingLink")
        val TourbookingLink: String? = null,
        @SerializedName("OLDTourBookingNo")
        val OLDTourBookingNo: String? = null,
        @SerializedName("Name")
        val Name: String? = null,
        @SerializedName("MobileNo")
        val MobileNo: String? = null,
        @SerializedName("Sector")
        val Sector: String? = null,
        @SerializedName("TravelType")
        val TravelType: String? = null,
        @SerializedName("Tour")
        val Tour: String? = null,
        @SerializedName("TourDateCode")
        val TourDateCode: String? = null,
        @SerializedName("TourStartDate")
        val TourStartDate: String? = null,
        @SerializedName("NoOfNights")
        val NoOfNights: Int? = null,
        @SerializedName("BookingStep")
        val BookingStep: String? = null,
        @SerializedName("BookBy")
        val BookBy: String? = null,
        @SerializedName("Branch")
        val Branch: String? = null,
        @SerializedName("TourBookingStatus")
        val TourBookingStatus: String? = null
)