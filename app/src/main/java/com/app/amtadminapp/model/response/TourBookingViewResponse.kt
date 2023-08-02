package com.app.amtadminapp.model.response

import com.google.gson.annotations.SerializedName

data class TourBookingViewResponse(
    @SerializedName("Data")
    val Data: ArrayList<TourBookingViewModel>? = null,
    @SerializedName("Details")
    val Details: Any?,
    @SerializedName("Message")
    val Message: String,
    @SerializedName("Status")
    val Status: Int
)

data class TourBookingViewModel (
    @SerializedName("ID")
    val ID: Int? = null,
    @SerializedName("SectorID")
    val SectorID: Int? = null,
    @SerializedName("Sector")
    val Sector: String? = null,
    @SerializedName("SectorType")
    val SectorType: String? = null,
    @SerializedName("SubSectorID")
    val SubSectorID: Int? = null,
    @SerializedName("SubSector")
    val SubSector: String? = null,
    @SerializedName("TourID")
    val TourID: Int? = null,
    @SerializedName("Tour")
    val Tour: String? = null,
    @SerializedName("CompanyID")
    val CompanyID: Int? = null,
    @SerializedName("Company")
    val Company: String? = null,
    @SerializedName("RoomTypeID")
    val RoomTypeID: Int? = null,
    @SerializedName("RoomType")
    val RoomType: String? = null,
    @SerializedName("IsLTCTour")
    val IsLTCTour: Boolean? = null,
    @SerializedName("NoOfNights")
    val NoOfNights: Int? = null,
    @SerializedName("TourBookingNo")
    val TourBookingNo: String? = null,
    @SerializedName("TravelType")
    val TravelType: String? = null,
    @SerializedName("TourMonth")
    val TourMonth: String? = null,
    @SerializedName("TourDate")
    val TourDate: String? = null,
    @SerializedName("TourDateCode")
    val TourDateCode: String? = null,
    @SerializedName("TourStartDate")
    val TourStartDate: String? = null,
    @SerializedName("TourEndDate")
    val TourEndDate: String? = null,
    @SerializedName("FirstName")
    val FirstName: String? = null,
    @SerializedName("LastName")
    val LastName: String? = null,
    @SerializedName("MobileNo")
    val MobileNo: String? = null,
    @SerializedName("EmailID")
    val EmailID: String? = null,
    @SerializedName("VehicleSharingPaxID")
    val VehicleSharingPaxID: Int? = null,
    @SerializedName("SpecialityTypeID")
    val SpecialityTypeID: Int? = null,
    @SerializedName("SpecialityType")
    val SpecialityType: String? = null,
    @SerializedName("VehicleSharing")
    val VehicleSharing: String? = null,
    @SerializedName("NoOfSeats")
    val NoOfSeats: Int? = null,
    @SerializedName("TotalNoOfRooms")
    val TotalNoOfRooms: Int? = null,
    @SerializedName("ExtraBed")
    val ExtraBed: Int? = null,
    @SerializedName("GroupBookingNo")
    val GroupBookingNo: String? = null,
    @SerializedName("PaxTypeList")
    val PaxTypeList: ArrayList<PaxTypeList>? = null,
    @SerializedName("DiscountType")
    val DiscountType: Int? = null,
    @SerializedName("DiscountAmount")
    val DiscountAmount: Double? = null,
    @SerializedName("AdminDiscountAmount")
    val AdminDiscountAmount: Double? = null,
    @SerializedName("DiscountValue")
    val DiscountValue: Double? = null,
    @SerializedName("TotalDiscountAmount")
    val TotalDiscountAmount: Double? = null,
    @SerializedName("TotalTCSAmount")
    val TotalTCSAmount: Double? = null,
    @SerializedName("TotalGSTAmount")
    val TotalGSTAmount: Double? = null,
    @SerializedName("Kasar")
    val Kasar: Double? = null,
    @SerializedName("ServiceChargeAmount")
    val ServiceChargeAmount: Double? = null,
    @SerializedName("TotalExtraCost")
    val TotalExtraCost: Double? = null,
    @SerializedName("TotalCost")
    val TotalCost: Double? = null,
    @SerializedName("TotalAmount")
    val TotalAmount: Double? = null
)

data class PaxTypeList (
    @SerializedName("PaxTypeID")
    val PaxTypeID: Int? = null,
    @SerializedName("NoOfRoom")
    val NoOfRoom: Int? = null
)

