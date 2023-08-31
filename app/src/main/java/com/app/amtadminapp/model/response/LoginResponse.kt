package com.app.amtadminapp.model.response

import com.google.gson.annotations.SerializedName

data class LoginResponse (
    @SerializedName("Status")
    var code: Int = 0,

    @SerializedName("Message")
    var message: String = "",

    @SerializedName("Data")
    var data: LoginModel? = null,

    @SerializedName("Details")
    var details: String = ""
)

data class LoginModel (
    @SerializedName("ID")
    var ID: Int = 0,

    @SerializedName("Name")
    var Name: String = "",

    @SerializedName("MobileNo")
    var MobileNo: String = "",

    @SerializedName("Details")
    var Details: String = "",

    @SerializedName("Token")
    var Token: String = ""
)
