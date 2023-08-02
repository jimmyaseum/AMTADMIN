package com.app.amtadminapp.model.response

import com.google.android.material.textfield.TextInputLayout
import com.google.gson.annotations.SerializedName

data class AllCityListResponse (
        @SerializedName("Data")
        val Data: ArrayList<AllCityListModel>? = null,
        @SerializedName("Message")
        val Message: String,
        @SerializedName("Status")
        val Status: Int
    )

data class AllCityListModel (
        @SerializedName("City")
        val City: String?,
        @SerializedName("CityID")
        val CityID: Int?,
        @SerializedName("Sector")
        val Sector: String?,
        @SerializedName("SectorID")
        val SectorID: Int?
    )

data class AllCityDataModel (
        @SerializedName("placeid")
        var placeid: Int = 0,

        @SerializedName("place")
        var place: String = "",

        @SerializedName("hotelid")
        var hotelid: Int = 0,

        @SerializedName("hotel")
        var hotel: String = "",

        @SerializedName("checkindate")
        var checkindate: String = "",

        @SerializedName("nights")
        var nights: String = "",

        @SerializedName("checkoutdate")
        var checkoutdate: String = "",

        @SerializedName("planid")
        var planid: Int = 0,

        @SerializedName("plan")
        var plan: String = "",

        //TIL Error handling
        @SerializedName("tilPlace")
        var tilPlace: TextInputLayout? = null,

        @SerializedName("tilHotel")
        var tilHotel: TextInputLayout? = null,

        @SerializedName("tilNights")
        var tilNights: TextInputLayout? = null,

        @SerializedName("tilCheckInDate")
        var tilCheckInDate: TextInputLayout? = null,

        @SerializedName("tilCheckOutDate")
        var tilCheckOutDate: TextInputLayout? = null,

        @SerializedName("tilPlan")
        var tilPlan: TextInputLayout? = null
)


data class AddMoreHotelDetailsModel (
        @SerializedName("placeid")
        var placeid: Int = 0,

        @SerializedName("place")
        var place: String = "",

        @SerializedName("hotelid")
        var hotelid: Int = 0,

        @SerializedName("hotel")
        var hotel: String = "",

        @SerializedName("checkindate")
        var checkindate: String = "",

        @SerializedName("nights")
        var nights: String = "",

        @SerializedName("checkoutdate")
        var checkoutdate: String = "",

        @SerializedName("roomcatid")
        var roomcatid: Int = 0,

        @SerializedName("roomcat")
        var roomcat: String = "",

        @SerializedName("room")
        var room: String = "",

        @SerializedName("planid")
        var planid: Int = 0,

        @SerializedName("plan")
        var plan: String = "",

        @SerializedName("extrabed")
        var extrabed: String = "",

        @SerializedName("pickupfrom")
        var pickupfrom: String = "",

        @SerializedName("pickuptime")
        var pickuptime: String = "",

        @SerializedName("by")
        var by: String = "",

        @SerializedName("supplierid")
        var supplierid: Int = 0,

        @SerializedName("supplier")
        var supplier: String = "",

        //TIL Error handling
        @SerializedName("tilPlace")
        var tilPlace: TextInputLayout? = null,

        @SerializedName("tilHotel")
        var tilHotel: TextInputLayout? = null,

        @SerializedName("tilNights")
        var tilNights: TextInputLayout? = null,

        @SerializedName("tilCheckInDate")
        var tilCheckInDate: TextInputLayout? = null,

        @SerializedName("tilCheckOutDate")
        var tilCheckOutDate: TextInputLayout? = null,

        @SerializedName("tilRoomCat")
        var tilRoomCat: TextInputLayout? = null,

        @SerializedName("tilRoom")
        var tilRoom: TextInputLayout? = null,

        @SerializedName("tilPlan")
        var tilPlan: TextInputLayout? = null,

        @SerializedName("tilExtrabed")
        var tilExtrabed: TextInputLayout? = null,

        @SerializedName("tilPickupFrom")
        var tilPickupFrom: TextInputLayout? = null,

        @SerializedName("tilPickuptime")
        var tilPickuptime: TextInputLayout? = null,

        @SerializedName("tilBy")
        var tilBy: TextInputLayout? = null,

        @SerializedName("tilSupplier")
        var tilSupplier: TextInputLayout? = null

        )