package com.app.amtadminapp.model.response

import com.google.gson.annotations.SerializedName

data class RemarksListResponse(
    @SerializedName("Data")
    val Data: ArrayList<RemarksListModel>? = null,
    @SerializedName("Details")
    val Details: Any?,
    @SerializedName("Message")
    val Message: String,
    @SerializedName("Status")
    val Status: Int
)

data class RemarksListModel (
    @SerializedName("ID")
    val ID: Int,
    @SerializedName("Remarks")
    val Remarks: String,
    @SerializedName("RemarksDate")
    val RemarksDate: String,
    @SerializedName("RemarksBy")
    val RemarksBy: String
    )
