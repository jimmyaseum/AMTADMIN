package com.app.amtadminapp.model

import android.net.Uri
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.annotations.SerializedName

data class TourBookingPaxResponse (
    @SerializedName("Data")
    val Data: TourBookingPaxModel? = null,
    @SerializedName("Details")
    val Details: Any?,
    @SerializedName("Message")
    val Message: String,
    @SerializedName("Status")
    val Status: Int
)

data class TourBookingPaxModel (
    @SerializedName("Name")
    val Name: String? = null,
    @SerializedName("MobileNo")
    val MobileNo: String? = null
)

data class TourBookingDetailsResponse (
    @SerializedName("Data")
    val Data: TourBookingDetailsModel? = null,
    @SerializedName("Details")
    val Details: Any?,
    @SerializedName("Message")
    val Message: String,
    @SerializedName("Status")
    val Status: Int
)

data class TourBookingDetailsModel (
    @SerializedName("ID")
    val ID: Int? = null,
    @SerializedName("HotelBookingNo")
    val HotelBookingNo: String? = null,
    @SerializedName("TourBookingNo")
    val TourBookingNo: String? = null,
    @SerializedName("SectorID")
    val SectorID: Int? = null,
    @SerializedName("SectorName")
    val SectorName: String? = null,
    @SerializedName("places")
    val places: ArrayList<PlaceInfo>? = ArrayList(),
    @SerializedName("CompanyID")
    val CompanyID: Int? = null,
    @SerializedName("CompanyName")
    val CompanyName: String? = null,
    @SerializedName("TourID")
    val TourID: Int? = null,
    @SerializedName("TourName")
    val TourName: String? = null,
    @SerializedName("TourDateCode")
    val TourDateCode: String? = null,
    @SerializedName("NoOfNights")
    val NoOfNights: Int? = null,
    @SerializedName("NoOfDays")
    val NoOfDays: Int? = null,
    @SerializedName("TourDate")
    val TourDate: String? = null,
    @SerializedName("PickupPlace")
    val PickupPlace: String? = null,
    @SerializedName("DropPlace")
    val DropPlace: String? = null,
    @SerializedName("Name")
    val Name: String? = null,
    @SerializedName("MobileNo")
    val MobileNo: String? = null,
    @SerializedName("RoomTypeID")
    val RoomTypeID: Int? = null,
    @SerializedName("RoomType")
    val RoomType: String? = null
)

//"TotalAdults": 2,
//"TotalExtraAdults": 0,
//"TotalCWB": 0,
//"TotalCNB": 0,
//"TotalInfants": 0,

data class PlaceInfo (
    @SerializedName("SectorID")
    val SectorID: Int? = null,
    @SerializedName("CityID")
    val CityID: Int? = null,
    @SerializedName("City")
    val City: String? = null
)

data class PlaceDataModel (
    @SerializedName("placeid")
    var placeid: Int = 0,

    @SerializedName("place")
    var place: String = "",

    @SerializedName("document")
    var document: Uri? = null,

    @SerializedName("documentname")
    var documentname: String? = null,

    //TIL Error handling
    @SerializedName("tilCity")
    var tilCity: TextInputLayout? = null,

    @SerializedName("tilUploadDocument")
    var tilUploadDocument: TextInputLayout? = null,

    @SerializedName("select_image")
    var select_image: ImageView? = null,

    @SerializedName("ll_Pdf")
    var ll_Pdf: LinearLayout? = null,

    @SerializedName("select_pdf")
    var select_pdf: TextView? = null
)