package com.app.amtadminapp.model.response

import com.google.gson.annotations.SerializedName

data class HotelBookingViewResponse (
    @SerializedName("Data")
    val Data: HotelBookingViewModel? = null,
    @SerializedName("Details")
    val Details: Any?,
    @SerializedName("Message")
    val Message: String,
    @SerializedName("Status")
    val Status: Int
        )

data class HotelBookingViewModel (
    @SerializedName("ID")
    val ID: Int? = null,
    @SerializedName("HotelBookingNo")
    val HotelBookingNo: String? = null,
    @SerializedName("TourBookingNo")
    val TourBookingNo: String? = null,
    @SerializedName("SectorID")
    var SectorID: Int? = null,
    @SerializedName("SectorName")
    var SectorName: String? = null,
    @SerializedName("CompanyID")
    var CompanyID: Int? = null,
    @SerializedName("CompanyName")
    var Company: String? = null,
    @SerializedName("TourID")
    var TourID: Int? = null,
    @SerializedName("TourName")
    var Tour: String? = null,
    @SerializedName("TourDateCode")
    var TourDateCode: String? = null,
    @SerializedName("TourDate")
    var TourDate: String? = null,

    @SerializedName("NoOfNights")
    var NoOfNights: Int? = null,
    @SerializedName("NoOfDays")
    var NoOfDays: Int? = null,
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
    @SerializedName("Total")
    val Total: Int? = null,
    @SerializedName("VehicleSharingPaxID")
    var VehicleSharingPaxID: Int? = null,
    @SerializedName("VehicleSharingPax")
    var VehicleSharingPax: String? = null,
    @SerializedName("BookByName")
    var BookByName: String? = null,
    @SerializedName("Remarks")
    var Remarks: String? = null,
    @SerializedName("BookBy")
    var BookBy: Int? = null,
    @SerializedName("RoomTypeID")
    var RoomTypeID: Int? = null,
    @SerializedName("places")
    var places: ArrayList<Hotelplaces>? = null,
    @SerializedName("PickupPlace")
    var PickupPlace: String? = null,
    @SerializedName("DropPlace")
    var DropPlace: String? = null
)

data class Hotelplaces (
    @SerializedName("ID")
    var ID: Int? = null,
    @SerializedName("HotelBookingID")
    val HotelBookingID: Int? = null,
    @SerializedName("CityID")
    var CityID: Int? = null,
    @SerializedName("City")
    var City: String? = null,
    @SerializedName("HotelID")
    var HotelID: Int? = null,
    @SerializedName("Hotel")
    var Hotel: String? = null,
    @SerializedName("SupplierID")
    var SupplierID: Int? = null,
    @SerializedName("Supplier")
    var Supplier: String? = null,
    @SerializedName("CheckInDate")
    var CheckInDate: String? = null,
    @SerializedName("CheckOutDate")
    var CheckOutDate: String? = null,
    @SerializedName("MealPlanID")
    val MealPlanID: Int? = null,
    @SerializedName("MealPlan")
    val MealPlan: String? = null,
    @SerializedName("RoomCategoryID")
    val RoomCategoryID: Int? = null,
    @SerializedName("RoomCategory")
    val RoomCategory: String? = null,
    @SerializedName("PickupFrom")
    val PickupFrom: String? = null,
    @SerializedName("PickupTime")
    val PickupTime: String? = null,
    @SerializedName("TravelBy")
    val TravelBy: String? = null,
    @SerializedName("TotalNoOfRooms")
    val TotalNoOfRooms: Int? = null,
    @SerializedName("NoOfNights")
    val NoOfNights: Int? = null,
    @SerializedName("ExtraBed")
    val ExtraBed: Int? = null

)