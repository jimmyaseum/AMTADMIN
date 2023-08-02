package com.app.amtadminapp.model.response

import com.google.gson.annotations.SerializedName

data class BranchListResponse (
    @SerializedName("Data")
    val Data: ArrayList<BranchListModel>? = null,
    @SerializedName("Message")
    val Message: String,
    @SerializedName("Status")
    val Status: Int
        )

data class BranchListModel (
    @SerializedName("ID")
    val ID: Int?,
    @SerializedName("BranchName")
    val BranchName: String?
        )