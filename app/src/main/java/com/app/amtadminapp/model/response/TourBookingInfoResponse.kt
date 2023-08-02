package com.app.amtadminapp.model.response
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.annotations.SerializedName

data class TourBookingInfoResponse (
    @SerializedName("Data")
    val Data: ArrayList<TourBookingInfoModel>? = null,
    @SerializedName("Details")
    val Details: Any?,
    @SerializedName("Message")
    val Message: String,
    @SerializedName("Status")
    val Status: Int
    )

data class TourBookingInfoModel (
    @SerializedName("CreatedBy")
    val CreatedBy: Int?,

    // region step 1

    @SerializedName("ID")
    val ID: Int?,
    @SerializedName("IsLTCTour")
    val IsLTCTour: Boolean?,
    @SerializedName("SectorID")
    val SectorID: Int?,
    @SerializedName("Sector")
    val Sector: String?,
    @SerializedName("SectorType")
    val SectorType: String?,
    @SerializedName("SubSectorID")
    val SubSectorID: Int?,
    @SerializedName("SubSector")
    val SubSector: String?,
    @SerializedName("TourID")
    val TourID: Int?,
    @SerializedName("Tour")
    val Tour: String?,
    @SerializedName("RoomTypeID")
    val RoomTypeID: Int?,
    @SerializedName("CompanyID")
    val CompanyID: Int?,
    @SerializedName("Company")
    val Company: String?,
    @SerializedName("RoomType")
    val RoomType: String?,
    @SerializedName("TourBookingNo")
    val TourBookingNo: String?,
    @SerializedName("TravelType")
    val TravelType: String?,
    @SerializedName("TourMonth")
    val TourMonth: String?,
    @SerializedName("TourDate")
    val TourDate: String?,
    @SerializedName("TourDateCode")
    val TourDateCode: String?,
    @SerializedName("TourStartDate")
    val TourStartDate: String?,
    @SerializedName("NoOfNights")
    val NoOfNights: Int?,
    @SerializedName("TourEndDate")
    val TourEndDate: String?,
    @SerializedName("FirstName")
    val FirstName: String?,
    @SerializedName("LastName")
    val LastName: String?,
    @SerializedName("MobileNo")
    val MobileNo: String?,
    @SerializedName("EmailID")
    val EmailID: String?,
    @SerializedName("TotalNoOfRooms")
    val TotalNoOfRooms: String?,
    @SerializedName("VehicleTypeID")
    val VehicleTypeID: Int?,
    @SerializedName("NoOfSeats")
    val NoOfSeats: Int?,
    @SerializedName("TotalAmount")
    val TotalAmount: Double?,
    @SerializedName("TotalGSTAmount")
    val TotalGSTAmount: String?,
    @SerializedName("TotalExtraCost")
    val TotalExtraCost: String?,
    @SerializedName("TotalCost")
    val TotalCost: String?,
    @SerializedName("VehicleSharingPaxID")
    val VehicleSharingPaxID: Int?,
    @SerializedName("SpecialityTypeID")
    val SpecialityTypeID: Int?,
    @SerializedName("SpecialityType")
    val SpecialityType: String?,
    @SerializedName("ExtraBed")
    val ExtraBed: Int?,
    @SerializedName("DiscountType")
    val DiscountType: Int?,
    @SerializedName("DiscountAmount")
    val DiscountAmount: Double?,
    @SerializedName("AdminDiscountAmount")
    val AdminDiscountAmount: Double?,
    @SerializedName("DiscountValue")
    val DiscountValue: Double?,
    @SerializedName("TotalDiscountAmount")
    val TotalDiscountAmount: Double?,
    @SerializedName("TotalTCSAmount")
    val TotalTCSAmount: Double?,
    @SerializedName("Kasar")
    val Kasar: Double?,
    @SerializedName("ServiceChargeAmount")
    val ServiceChargeAmount: Double?,
    @SerializedName("VehicleType")
    val VehicleType: String?,

    //endregion

    // region step 2
    @SerializedName("TourPlaceBooking")
    val TourPlaceBooking: ArrayList<TourPlaceBookingModel>? = null,

    // endregion

    // region step 3

    @SerializedName("Address")
    val Address: String?,
    @SerializedName("CityID")
    val CityID: Int?,
    @SerializedName("City")
    val CityName: String?,
    @SerializedName("StateID")
    val StateID: Int?,
    @SerializedName("State")
    val StateName: String?,
    @SerializedName("CountryID")
    val CountryID: Int?,
    @SerializedName("Country")
    val CountryName: String?,
    @SerializedName("MobileNoDuringTravelling")
    val MobileNoDuringTravelling: String?,
    @SerializedName("ResidentPhoneNo")
    val ResidentPhoneNo: String?,
    @SerializedName("EmergencyNo")
    val EmergencyNo: String?,
    @SerializedName("PANCardNo")
    val PANCardNo: String?,
    @SerializedName("PassportNo")
    val PassportNo: String?,
    @SerializedName("AadharNo")
    val AadharNo: String?,
    @SerializedName("IsCompanyInvoice")
    val IsCompanyInvoice: Boolean = false,
    @SerializedName("CompanyName")
    val CompanyName: String?,
    @SerializedName("CompanyAddress")
    val CompanyAddress: String?,
    @SerializedName("CompanyGSTNo")
    val CompanyGSTNo: String?,
    @SerializedName("CompanyPANNo")
    val CompanyPANNo: String?,
    @SerializedName("CompanyCityID")
    val CompanyCityID: Int?,
    @SerializedName("CompanyCity")
    val CompanyCityName: String?,
    @SerializedName("CompanyStateID")
    val CompanyStateID: Int?,
    @SerializedName("CompanyState")
    val CompanyStateName: String?,
    @SerializedName("CompanyCountryID")
    val CompanyCountryID: Int?,
    @SerializedName("CompanyCountry")
    val CompanyCountryName: String?,

    //endregion

    //region step 4

    @SerializedName("roomPaxes")
    val roomPaxes: ArrayList<TourPaxInformationModel>? = null,

    // endregion

    // region step 5
    @SerializedName("PickUpDate")
    val PickUpDate: String?,
    @SerializedName("PickUpPlace")
    val PickUpPlace: String?,
    @SerializedName("PickUpTime")
    val PickUpTime: String?,
    @SerializedName("DropDate")
    val DropDate: String?,
    @SerializedName("DropPlace")
    val DropPlace: String?,
    @SerializedName("DropTime")
    val DropTime: String?,
    @SerializedName("RouteVoucherNo")
    val RouteVoucherNo: String?,
    @SerializedName("VendorID")
    val VendorID: Int?,
    @SerializedName("Vendor")
    val Vendor: String?,
    @SerializedName("TicketBookedBy")
    val TicketBookedBy: Int?,
    @SerializedName("FlightNo")
    val FlightNo: String?,
    @SerializedName("FlightTicketNo")
    val FlightTicketNo: String?,
    @SerializedName("HotelVoucherNo")
    val HotelVoucherNo: String?,
    @SerializedName("BusSeat")
    val BusSeat: String?



    //endregion
    )

data class TourPaxInformationModel(
    @SerializedName("RoomNo")
    val RoomNo: String?,
    @SerializedName("PaxTypeID")
    val PaxTypeID: Int?,
    @SerializedName("TouristPaxInfo")
    val paxData: ArrayList<paxDataModel>? = null
)

data class paxDataModel (
    @SerializedName("ID")
    val ID: Int?,
    @SerializedName("TourBookingID")
    val TourBookingID: Int?,
    @SerializedName("RoomNo")
    val RoomNo: String?,
    @SerializedName("Initial")
    val Initial: String?,
    @SerializedName("FirstName")
    val FirstName: String?,
    @SerializedName("LastName")
    val LastName: String?,
    @SerializedName("MobileNo")
    val MobileNo: String?,
    @SerializedName("Gender")
    val Gender: String?,
    @SerializedName("DOB")
    val DOB: String?,
    @SerializedName("Age")
    val Age: String?,
    @SerializedName("PassportNo")
    val PassportNo: String?,
    @SerializedName("DiscountAmount")
    val DiscountAmount: Double? = 0.0,
    @SerializedName("Amount")
    val Amount: Double? = 0.0,
    @SerializedName("ExtraCost")
    val ExtraCost: Double? = 0.0,
    @SerializedName("Remarks")
    val Remarks: String?,
    @SerializedName("IsVisa")
    val IsVisa: Boolean?,
    @SerializedName("IsInsurance")
    val IsInsurance: Boolean?
)


data class TPIM (
    @SerializedName("roomNo")
    var roomNo: String = "",

    @SerializedName("paxTypeID")
    var paxTypeID: Int = 0,

    @SerializedName("paxType")
    var paxType: String = "",

    @SerializedName("paxData")
    var paxData: ArrayList<paxDataModel>,

    //TIL Error handling

    @SerializedName("txtRoomNo")
    var txtRoomNo: TextView? = null,

    @SerializedName("tilPaxType")
    var tilPaxType: TextInputLayout? = null,

    @SerializedName("rvPaxInfoInner")
    var rvPaxInfoInner: RecyclerView? = null

)

data class TourPlaceBookingModel (
    @SerializedName("ID")
    val ID: Int? = null,
    @SerializedName("TourBookingID")
    val TourBookingID: Int? = null,
    @SerializedName("TourID")
    val TourID: Int? = null,
    @SerializedName("PlaceID")
    val PlaceID: Int? = null,
    @SerializedName("City")
    val City: String? = null,
    @SerializedName("CheckinDate")
    val CheckinDate: String? = null,
    @SerializedName("NoOfNights")
    val NoOfNights: Int? = null,
    @SerializedName("NoOfDays")
    val NoOfDays: Int? = null,
    @SerializedName("CheckOutDate")
    val CheckOutDate: String? = null,
    @SerializedName("Position")
    val Position: Int? = null,
    @SerializedName("Remarks")
    val Remarks: String? = "",
    @SerializedName("HotelID")
    val HotelID: Int? = 0,
    @SerializedName("HotelName")
    val HotelName: String? = "",
    @SerializedName("MealPlanID")
    val MealPlanID: Int? = 0
    )


/*
            "NoOfSeats": 6,
            "Remarks": null,
            "AvailableNoOfRooms": null,
            "TotalNoOfSeats": 6,
            "AvailableNoOfSeats": 6,
            "PinCode": null,
            "TotalAdults": 6,
            "TotalExtraAdults": 0,
            "TotalCWB": 0,
            "TotalCNB": 0,
            "TotalInfants": 0,
            "TotalNoOfPax": 6,
            "Gender": null,
            "TourPlaceBooking": [],
            "OldTourBookingNo": null,
            "PaxTypeList": [
                {
                    "PaxTypeID": 1,
                    "NoOfRoom": 3
                }
            ],
            */