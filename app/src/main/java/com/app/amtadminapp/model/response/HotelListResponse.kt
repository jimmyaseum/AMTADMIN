package com.app.amtadminapp.model.response

import com.google.gson.annotations.SerializedName

data class HotelListResponse (
        @SerializedName("Data")
        val Data: ArrayList<HotelListModel>? = null,
        @SerializedName("Details")
        val Details: Any?,
        @SerializedName("Message")
        val Message: String,
        @SerializedName("Status")
        val Status: Int
        )

data class HotelListModel (
        @SerializedName("ID")
        val ID: Int? = 0,
        @SerializedName("HotelName")
        val HotelName: String? = ""
        )