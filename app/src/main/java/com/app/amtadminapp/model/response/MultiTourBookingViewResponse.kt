package com.app.amtadminapp.model.response

import com.google.gson.annotations.SerializedName

data  class MultiTourBookingViewResponse (
    @SerializedName("Data")
    val Data: ArrayList<MultiTourBookingViewModel>? = null,
    @SerializedName("Details")
    val Details: Any?,
    @SerializedName("Message")
    val Message: String,
    @SerializedName("Status")
    val Status: Int
    )

data class MultiTourBookingViewModel (
    @SerializedName("ID")
    val ID: Int? = null,
    @SerializedName("SectorID")
    var SectorID: Int? = null,
    @SerializedName("Sector")
    var Sector: String? = null,
    @SerializedName("TourDateCode")
    var TourDateCode: String? = null,
    @SerializedName("TourBookingNo")
    val TourBookingNo: String? = null,
    @SerializedName("TourID")
    var TourID: Int? = null,
    @SerializedName("Tour")
    var Tour: String? = null,
    @SerializedName("CompanyID")
    var CompanyID: Int? = null,
    @SerializedName("Company")
    var Company: String? = null,
    @SerializedName("TourDate")
    var TourDate: String? = null,
    @SerializedName("NoOfNights")
    var NoOfNights: Int? = null,
    @SerializedName("TotalAdults")
    var TotalAdults: Int? = null,
    @SerializedName("TotalExtraAdults")
    var TotalExtraAdults: Int? = null,
    @SerializedName("TotalCWB")
    var TotalCWB: Int? = null,
    @SerializedName("TotalCNB")
    var TotalCNB: Int? = null,
    @SerializedName("TotalInfants")
    var TotalInfants: Int? = null,
    @SerializedName("TotalNoOfPax")
    val TotalNoOfPax: Int? = null,
    @SerializedName("RoomTypeID")
    var RoomTypeID: Int? = null,
    @SerializedName("TotalNoOfRooms")
    val TotalNoOfRooms: Int? = null,
    @SerializedName("ExtraBed")
    val ExtraBed: Int? = null,
    @SerializedName("PickupPlace")
    var PickupPlace: String? = null,
    @SerializedName("DropPlace")
    var DropPlace: String? = null,

    @SerializedName("roomPaxes")
    val roomPaxes: ArrayList<roomPaxes>? = null,
    @SerializedName("places")
    val places: ArrayList<places>? = null
    )

data class roomPaxes (
    @SerializedName("RoomNo")
    val RoomNo: String? = null,
    @SerializedName("TourBookingID")
    val TourBookingID: Int? = null,
    @SerializedName("PaxTypeID")
    val PaxTypeID: Int? = null,
    @SerializedName("TouristPaxInfo")
    val TouristPaxInfo: ArrayList<TouristPaxInfo>? = null
    )
data class TouristPaxInfo (
    @SerializedName("ID")
    val ID: Int? = null,
    @SerializedName("TourBookingID")
    val TourBookingID: Int? = null,
    @SerializedName("TourID")
    val TourID: Int? = null,
    @SerializedName("FirstName")
    val FirstName: String? = null,
    @SerializedName("LastName")
    val LastName: String? = null,
    @SerializedName("MobileNo")
    val MobileNo: String? = null,
    @SerializedName("PNR")
    val PNR: String? = null,
    @SerializedName("RoomNo")
    val RoomNo: String? = null,
    @SerializedName("MultiBookingRoomNo")
    val MultiBookingRoomNo: Int? = null,
    @SerializedName("PaxType")
    val PaxType: String? = null
    )

data class places (
    @SerializedName("CityID")
    val CityID: Int? = null,
    @SerializedName("City")
    val City: String? = null,
    @SerializedName("TourID")
    val TourID: Int? = null,
    @SerializedName("Tour")
    val Tour: String? = null,
    @SerializedName("HotelID")
    val HotelID: Int? = null,
    @SerializedName("NoOfNights")
    val NoOfNights: Int? = null,
    @SerializedName("CheckInDate")
    val CheckInDate: String? = null,
    @SerializedName("CheckOutDate")
    val CheckOutDate: String? = null,
    @SerializedName("MealPlanID")
    val MealPlanID: Int? = null,
    @SerializedName("RoomCategoryID")
    val RoomCategoryID: Int? = null,
    @SerializedName("HotelList")
    val HotelList: ArrayList<HotelList>? = null

    )
data class HotelList (
    @SerializedName("ID")
    val ID: Int? = null,
    @SerializedName("HotelName")
    val HotelName: String? = null,
    @SerializedName("CityID")
    val CityID: Int? = null,
    @SerializedName("IsDefault")
    val IsDefault: Boolean? = false
)

