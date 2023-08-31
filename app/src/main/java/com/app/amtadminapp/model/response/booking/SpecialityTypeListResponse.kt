package com.app.amtadminapp.model.response.booking

import com.google.gson.annotations.SerializedName

data class SpecialityTypeListResponse (
        @SerializedName("Data")
        val Data: ArrayList<SpecialityTypeListModel>? = null,
        @SerializedName("Message")
        val Message: String,
        @SerializedName("Status")
        val Status: Int
)

data class SpecialityTypeListModel (
        @SerializedName("ID")
        val ID: Int?,
        @SerializedName("Title")
        val Title: String?,
        @SerializedName("Prefix")
        val Prefix: String?,
        @SerializedName("SpecialityURL")
        val SpecialityURL: String?
)
