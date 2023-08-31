package com.app.amtadminapp.model.response

import com.google.gson.annotations.SerializedName

data class CityResponse(
    @SerializedName("Status")
    var code: Int = 0,

    @SerializedName("Message")
    var message: String = "",

    @SerializedName("Data")
    var data: ArrayList<CityModel>? = null,

    @SerializedName("Details")
    var details: String = ""
)
data class CityModel (
    @SerializedName("ID")
    val CityID: Int = 0,
    @SerializedName("CityName")
    var CityName: String = "",
    @SerializedName("StateID")
    val StateID: Int = 0,
    @SerializedName("StateName")
    var StateName: String = "",
    @SerializedName("CountryID")
    val CountryID: Int = 0,
    @SerializedName("CountryName")
    var CountryName: String = ""
)

