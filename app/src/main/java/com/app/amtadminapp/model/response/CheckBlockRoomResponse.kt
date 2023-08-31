package com.app.amtadminapp.model.response

import com.google.gson.annotations.SerializedName

data class CheckBlockRoomResponse(
    @SerializedName("Data")
    val Data: CheckBlockModel? = null,
    @SerializedName("Details")
    val Details: Any?,
    @SerializedName("Message")
    val Message: String,
    @SerializedName("Status")
    val Status: Int
)
data class CheckBlockModel (
    @SerializedName("BalanceRoom")
    val BalanceRoom: Int? = null

        )