package com.app.amtadminapp.model.response

import com.google.gson.annotations.SerializedName

data class SelectUserResponse(

    @SerializedName("Result")
    var Result: String? = null,

    @SerializedName("Data")
    var Data: ArrayList<SelectUserModel>? = arrayListOf(),

    @SerializedName("Details")
    var Details: String? = null,

    @SerializedName("Message")
    var Message: String? = null,

    @SerializedName("Status")
    var Status: Int? = null,

    @SerializedName("CachingStatus")
    var CachingStatus: String? = null,

    @SerializedName("ItemCount")
    var ItemCount: Int? = null,

    @SerializedName("TotalPages")
    var TotalPages: String? = null
)

data class SelectUserModel (

    @SerializedName("BranchID")
    var BranchID: Int? = null,

    @SerializedName("EmployeeID")
    var EmployeeID: Int? = null,

    @SerializedName("EmployeeName")
    var EmployeeName: String? = null,

    @SerializedName("Message")
    var Message: String? = null,

    @SerializedName("Details")
    var Details: String? = null
)