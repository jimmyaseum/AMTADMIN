package com.app.amtadminapp.model.response

import com.google.gson.annotations.SerializedName

class CommonResponse {

    @SerializedName("Status")
    var code: Int = 0

    @SerializedName("Message")
    var message: String = ""

    @SerializedName("Details")
    var Details: String = ""
}
class CommonResponse2 {

    @SerializedName("Status")
    var code: Int = 0

    @SerializedName("Message")
    var message: String = ""

    @SerializedName("Details")
    var Details: String = ""

    @SerializedName("Data")
    var Data: CommonModel? = null
}
data class CommonModel (
    @SerializedName("ID")
    var ID: Int = 0,
    @SerializedName("CustomerID")
    var CustomerID: Int = 0,
    @SerializedName("BookingNo")
    var BookingNo: String = ""
)