package com.app.amtadminapp.model.response.booking

import com.google.gson.annotations.SerializedName

data class CompanyListResponse (
        @SerializedName("List")
        val Data: ArrayList<CompanyListModel>? = null,
        @SerializedName("Message")
        val Message: String,
        @SerializedName("Status")
        val Status: Int
    )
data class CompanyListModel
    (
    @SerializedName("CompanyName")
    val CompanyName: String?,
    @SerializedName("Address")
    val Address: String?,
    @SerializedName("CityID")
    val CityID: Int?,
    @SerializedName("City")
    val City: String?,
    @SerializedName("StateID")
    val StateID: Int?,
    @SerializedName("State")
    val State: String?,
    @SerializedName("CountryID")
    val CountryID: Int?,
    @SerializedName("Country")
    val Country: String?,

    @SerializedName("PinCode")
    val PinCode: String?,
    @SerializedName("OwnerName")
    val OwnerName: String?,
    @SerializedName("MobileNo")
    val MobileNo: String?,
    @SerializedName("EmailID")
    val EmailID: String?,
    @SerializedName("PANNo")
    val PANNo: String?,
    @SerializedName("PANCopy")
    val PANCopy: String?,
    @SerializedName("GSTNo")
    val GSTNo: String?,
    @SerializedName("GSTCopy")
    val GSTCopy: String?,
    @SerializedName("AadharNo")
    val AadharNo: String?,
    @SerializedName("AadharCopy")
    val AadharCopy: String?,
    @SerializedName("ID")
    val ID: Int?
    )

