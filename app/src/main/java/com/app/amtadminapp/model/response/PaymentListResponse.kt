package com.app.amtadminapp.model.response

import com.google.gson.annotations.SerializedName

data class PaymentListResponse(
    @SerializedName("Data")
    val Data: ArrayList<PaymentListModel>? = null,
    @SerializedName("Details")
    val Details: Any?,
    @SerializedName("Message")
    val Message: String,
    @SerializedName("Status")
    val Status: Int
)

data class PaymentListModel (
    @SerializedName("PaymentDate")
    val PaymentDate: String? = null,
    @SerializedName("PaymentFor")
    val PaymentFor: String? = null,
    @SerializedName("ReceiptNo")
    val ReceiptNo: String? = null,
    @SerializedName("OldReceiptNo")
    val OldReceiptNo: String? = null,
    @SerializedName("TourBookingNo")
    val TourBookingNo: String? = null,
    @SerializedName("TourbookingLink")
    val TourbookingLink: String? = null,
    @SerializedName("PaymentLink")
    val PaymentLink: String? = null,
    @SerializedName("Name")
    val Name: String? = null,
    @SerializedName("CompanyName")
    val CompanyName: String? = null,
    @SerializedName("PaymentType")
    val PaymentType: String? = null,
    @SerializedName("BookBy")
    val BookBy: String? = null,
    @SerializedName("Branch")

    val Branch: String? = null

    val Branch: String? = null,
    @SerializedName("Amount")
    val Amount: String?,
    @SerializedName("ReceiptImage")
    val ReceiptImage: String?,
    @SerializedName("IsCreatedFromApp")
    val IsCreatedFromApp: Boolean? = false


)

