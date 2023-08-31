package com.app.amtadminapp.model.response

import com.google.gson.annotations.SerializedName

data class NotificationListResponse(

    @SerializedName("Data")
    val Data: ArrayList<NotificationListModel>? = null,
    @SerializedName("Details")
    val Details: Any?,
    @SerializedName("Message")
    val Message: String,
    @SerializedName("Status")
    val Status: Int

)
data class NotificationListModel (
    @SerializedName("ID")
    val ID: Int?,
    @SerializedName("UserID")
    val UserID: Int?,
    @SerializedName("UserType")
    val UserType: Int?,
    @SerializedName("MessageType")
    val MessageType: String?,
    @SerializedName("ReferenceID")
    val ReferenceID: Int?,
    @SerializedName("NotificationDate")
    val NotificationDate: String?, // "12/21/2022 19:09:41",
    @SerializedName("Title")
    val Title: String?,
    @SerializedName("Description")
    val Description: String?,
    @SerializedName("TourBookingNo")
    val TourBookingNo: String?,
    @SerializedName("CustomerID")
    val CustomerID: Int?

    )
