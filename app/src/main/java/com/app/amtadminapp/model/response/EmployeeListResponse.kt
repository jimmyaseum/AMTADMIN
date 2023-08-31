package com.app.amtadminapp.model.response

import com.google.gson.annotations.SerializedName

data class EmployeeListResponse (
    @SerializedName("Data")
    val Data: ArrayList<EmployeeListModel>? = null,
    @SerializedName("Message")
    val Message: String,
    @SerializedName("Status")
    val Status: Int
)

data class EmployeeListModel (
    @SerializedName("ID")
    val ID: Int?,
    @SerializedName("FirstName")
    val FirstName: String?,
    @SerializedName("LastName")
    val LastName: String?
    )