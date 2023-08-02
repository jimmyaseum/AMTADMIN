package com.app.amtadminapp.model.response

import com.google.gson.annotations.SerializedName

data class HotelBlockReportResponse(

    @SerializedName("Result")
    var Result: String? = null,

    @SerializedName("Data")
    var Data: HotelBlockReportModel? = null,

    @SerializedName("Details" )
    var Details: String? = null,

    @SerializedName("Message" )
    var Message: String? = null,

    @SerializedName("Status"  )
    var Status: Int? = null,

    @SerializedName("CachingStatus" )
    var CachingStatus: String? = null,

    @SerializedName("ItemCount" )
    var ItemCount: Int? = null,

    @SerializedName("TotalPages")
    var TotalPages: String? = null
)

data class HotelBlockReportModel (
    @SerializedName("ID")
    var ID: Int? = null,

    @SerializedName("HotelName")
    var HotelName: String? = null,

    @SerializedName("HotelAddress")
    var HotelAddress: String? = null,

    @SerializedName("CityName")
    var CityName: String? = null,

    @SerializedName("StateName")
    var StateName: String? = null,

    @SerializedName("CountryName")
    var CountryName: String? = null,

    @SerializedName("LandMark")
    var LandMark: String? = null,

    @SerializedName("StarRating")
    var StarRating: String? = null,

    @SerializedName("Message")
    var Message: String? = null,

    @SerializedName("Details")
    var Details: String? = null,

    @SerializedName("hotelLists")
    var hotelLists: ArrayList<HotelBlockListModel> = arrayListOf()
)

data class HotelBlockListModel (
    @SerializedName("HotelID")
    var HotelID: Int?= null,

    @SerializedName("HotelName")
    var HotelName: String? = null,

    @SerializedName("HotelBlockID")
    var HotelBlockID: Int?= null,

    @SerializedName("TourDateCode")
    var TourDateCode: String? = null,

    @SerializedName("TourName")
    var TourName: String? = null,

    @SerializedName("RoomTypeID")
    var RoomTypeID: Int?= null,

    @SerializedName("CheckInDate")
    var CheckInDate: String? = null,

    @SerializedName("CheckOutDate")
    var CheckOutDate: String? = null,

    @SerializedName("TotalNights")
    var TotalNights: String? = null,

    @SerializedName("NoOfRooms")
    var NoOfRooms: String? = null,

    @SerializedName("ConfirmedRooms")
    var ConfirmedRooms: String? = null,

    @SerializedName("ExtraBed")
    var ExtraBed: String? = null,

    @SerializedName("AvailableRooms")
    var AvailableRooms: String? = null
)