package com.app.amtadminapp.model.response.booking

import com.google.gson.annotations.SerializedName

data class RoomTypeListResponse
    (
        @SerializedName("List")
        val Data: ArrayList<RoomTypeListModel>? = null,
        @SerializedName("Message")
        val Message: String,
        @SerializedName("Status")
        val Status: Int
    )
data class RoomTypeListModel
    (
    @SerializedName("PinCode")
    val PinCode: String?,
    @SerializedName("Title")
    val Title: String?,
    @SerializedName("Prefix")
    val Prefix: String?,
    @SerializedName("ID")
    val ID: Int?
    )

