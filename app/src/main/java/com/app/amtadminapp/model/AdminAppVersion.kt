package com.app.amtadminapp.model

import com.google.gson.annotations.SerializedName

data class AdminAppVersion(
    @SerializedName("Data")
    val Data: Setting? = null,
    @SerializedName("Details")
    val Details: Any?,
    @SerializedName("Message")
    val Message: String,
    @SerializedName("Status")
    val Status: Int
)
data class Setting (
    @SerializedName("Version")
    val Version: String
)