package com.app.amtadminapp.model.response.booking

import com.google.gson.annotations.SerializedName

data class MealListResponse(
    @SerializedName("List")
    val Data: ArrayList<MealListModel>? = null,
    @SerializedName("Message")
    val Message: String,
    @SerializedName("Status")
    val Status: Int
)
data class MealListModel
    (
    @SerializedName("Title")
    val Title: String?,
    @SerializedName("ID")
    val ID: Int?,
    @SerializedName("IsDelete")
    val IsDelete: Boolean?
)

