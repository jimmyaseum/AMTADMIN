package com.app.amtadminapp.model.response.booking

import com.google.gson.annotations.SerializedName

data class TourInfoResponse (
        @SerializedName("Data")
        val Data: ArrayList<TourInfoModel>? = null,
        @SerializedName("Details")
        val Details: Any?,
        @SerializedName("Message")
        val Message: String,
        @SerializedName("Status")
        val Status: Int
        )

data class TourInfoModel (
        @SerializedName("ID")
        val ID: Int?,
        @SerializedName("TourID")
        val TourID: Int?,
        @SerializedName("TourName")
        val TourName: String?,
        @SerializedName("CompanyID")
        val CompanyID: Int?,
        @SerializedName("CompanyName")
        val CompanyName: String?,
        @SerializedName("NoOfNights")
        val NoOfNights: Int?,
        @SerializedName("monthData")
        val monthData: ArrayList<MonthDataModel>? = null,
        @SerializedName("GSTPer")
        val GSTPer: Int?,
        @SerializedName("TCSPer")
        val TCSPer: Int?,
        @SerializedName("IsApplyOnServiceTax")
        val IsApplyOnServiceTax: Boolean?


)

data class MonthDataModel (
        @SerializedName("Month")
        val Month: Int?,
        @SerializedName("MonthName")
        val MonthName: String?,
        @SerializedName("tourdateData")
        val DateData: ArrayList<DateDataModel>? = null
)

data class DateDataModel(
        @SerializedName("ID")
        val ID: Int?,
        @SerializedName("TourDate")
        val TourDate: String?,
        @SerializedName("MonthID")
        val MonthID: Int?,
        @SerializedName("TourDateCode")
        val TourDateCode: String?
)



