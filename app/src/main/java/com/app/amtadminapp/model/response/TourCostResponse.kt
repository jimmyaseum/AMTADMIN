package com.app.amtadminapp.model.response

import com.google.gson.annotations.SerializedName

data class TourCostResponse (
    @SerializedName("Data")
    val Data: TourCostModel? = null,
    @SerializedName("Message")
    val Message: String,
    @SerializedName("Status")
    val Status: Int
        )

data class TourCostModel (
    @SerializedName("TotalAmount")
    val TotalAmount: Double?,
    @SerializedName("SectorType")
    val SectorType: String?
   )
