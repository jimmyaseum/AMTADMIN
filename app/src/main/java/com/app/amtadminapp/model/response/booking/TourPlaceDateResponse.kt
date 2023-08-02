package com.app.amtadminapp.model.response.booking

import com.google.gson.annotations.SerializedName

data class TourPlaceDateResponse (
        @SerializedName("Data")
        val Data: ArrayList<TourPlaceDateModel>? = null,
        @SerializedName("Details")
        val Details: Any?,
        @SerializedName("Message")
        val Message: String,
        @SerializedName("Status")
        val Status: Int
    )

data class TourPlaceDateModel (
        @SerializedName("ID")
        val ID: Int? = 0,
        @SerializedName("PlaceID")
        val PlaceID: Int? = 0,
        @SerializedName("Place")
        val Place: String? = "",
        @SerializedName("Position")
        val Position: Int? = 0,
        @SerializedName("NoOfNights")
        val NoOfNights: Int? = 0,
        @SerializedName("CheckinDate")
        val CheckinDate: String? = "",
        @SerializedName("CheckOutDate")
        val CheckOutDate: String? = "",
        @SerializedName("Remarks")
        val Remarks: String? = ""
    )

