package com.app.amtadminapp.model.response.booking

import com.google.gson.annotations.SerializedName

data class RoomCategoryListResponse(
    @SerializedName("Data")
    val Data: ArrayList<RoomCategoryListModel>? = null,
    @SerializedName("Message")
    val Message: String,
    @SerializedName("Status")
    val Status: Int
)
data class RoomCategoryListModel
    (
    @SerializedName("Title")
    val Title: String?,
    @SerializedName("ID")
    val ID: Int?,
    @SerializedName("IsDelete")
    val IsDelete: Boolean?
)
