package com.app.amtadminapp.model.response

import com.google.gson.annotations.SerializedName

data class RelationResponse(
    @SerializedName("Status")
    var code: Int = 0,

    @SerializedName("Message")
    var message: String = "",

    @SerializedName("Data")
    var data: ArrayList<RelationModel>? = null,

    @SerializedName("Details")
    var details: String = ""
)
data class RelationModel (
    @SerializedName("ID")
    val ID: Int = 0,
    @SerializedName("Name")
    var Name: String = ""
)


