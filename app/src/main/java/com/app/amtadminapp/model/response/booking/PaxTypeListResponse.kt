package com.app.amtadminapp.model.response.booking

import com.google.gson.annotations.SerializedName

data class PaxTypeListResponse (
        @SerializedName("Data")
        val Data: ArrayList<PaxTypeListModel>? = null,
        @SerializedName("Message")
        val Message: String,
        @SerializedName("Status")
        val Status: Int
)

data class PaxTypeListModel (
        @SerializedName("ID")
        val ID: Int?,
        @SerializedName("PaxType")
        val PaxType: String?,
        @SerializedName("Adult")
        val Adult: Int?,
        @SerializedName("CWB")
        val CWB: Int?,
        @SerializedName("CNB")
        val CNB: Int?,
        @SerializedName("Infant")
        val Infant: Int?,
        @SerializedName("ExtraAdult")
        val ExtraAdult: Int?,
        @SerializedName("PaxCount")
        val PaxCount: Int?
)
