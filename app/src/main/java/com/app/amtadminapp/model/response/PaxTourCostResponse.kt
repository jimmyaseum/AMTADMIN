package com.app.amtadminapp.model.response

import com.google.gson.annotations.SerializedName

data class PaxTourCostResponse(
    @SerializedName("Data")
    val Data: ArrayList<PaxTourCostModel>? = null,
    @SerializedName("Details")
    val Details: Any?,
    @SerializedName("Message")
    val Message: String,
    @SerializedName("Status")
    val Status: Int
)
data class PaxTourCostModel (
    @SerializedName("Rate")
    val Rate: Int? = 0,
    @SerializedName("ExtraCost")
    val ExtraCost: Double? = 0.0,
    @SerializedName("Discount")
    val Discount: Double? = 0.0,
    @SerializedName("PaxType")
    val PaxType: String? = null
        )