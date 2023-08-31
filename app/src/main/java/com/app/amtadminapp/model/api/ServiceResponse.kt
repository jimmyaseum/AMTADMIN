package com.app.amtadminapp.model.api

import com.google.gson.annotations.SerializedName

data class ServiceResponse (
    @SerializedName("Result")
    val Result: Any?,
    @SerializedName("Data")
    val Data: Any?,
    @SerializedName("Details")
    val Details: Any?,
    @SerializedName("Message")
    val Message: String,
    @SerializedName("Status")
    val Status: Int,
    @SerializedName("CachingStatus")
    val CachingStatus: Any?,
    @SerializedName("ItemCount")
    val ItemCount: Int,
    @SerializedName("TotalPages")
    val TotalPages: Any?
)
