package com.app.amtadminapp.model.response

import com.google.gson.annotations.SerializedName

data class VenderListResponse (
    @SerializedName("Data")
    val Data: ArrayList<VenderListModel>? = null,
    @SerializedName("Details")
    val Details: Any?,
    @SerializedName("Message")
    val Message: String,
    @SerializedName("Status")
    val Status: Int
    )

data class VenderListModel (
    @SerializedName("ID")
    val ID: Int?,
    @SerializedName("VendorName")
    val VendorName: String?,
    @SerializedName("VendorType")
    val VendorType: String?,
    @SerializedName("VendorTypeID")
    val VendorTypeID: Int?
    )