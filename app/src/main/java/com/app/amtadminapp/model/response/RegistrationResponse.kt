package com.app.amtadminapp.model.response

import com.google.gson.annotations.SerializedName

data class RegistrationResponse (
    @SerializedName("Status")
    var code: Int = 0,

    @SerializedName("Message")
    var message: String = "",

    @SerializedName("Data")
    var data: RegistrationModel? = null,

    @SerializedName("Details")
    var details: String = ""

)
data class RegistrationModel (
    @SerializedName("ID")
    var ID: Int = 0,

    @SerializedName("UserTypeID")
    var UserTypeID: Int = 0,

    @SerializedName("Name")
    var Name: String = "",

    @SerializedName("FirstName")
    var FirstName: String = "",

    @SerializedName("LastName")
    var LastName: String = "",

    @SerializedName("UserName")
    var UserName: String = "",

    @SerializedName("EmailID")
    var EmailID: String = "",

    @SerializedName("MobileNo")
    var MobileNo: String = "",

    @SerializedName("Details")
    var Details: String = "",

    @SerializedName("Token")
    var Token: String = "",

    @SerializedName("CustomerImage")
    var CustomerImage: String = ""
)



    /*"Master": false,
    "HotelBlock": false,
    "AirlineBlock": false,
    "TourBooking": false,
    "TemporaryTourBooking": false,
    "HotelVoucher": false,
    "RouteVoucher": false,
    "AirlineVoucher": false,
    "PaymentReceipt": false,
    "Report": false,
    "mainRoleRights": {
        "Dashboard": true,
        "Block": true,
        "Booking": true,
        "Voucher": true,
        "Receipt": true,
        "Reports": true,
        "Expense": true,
        "Tour": true,
        "Air": true,
        "Hotel": true,
        "Account": true
    },
    "subRoleRights": {
        "HotelBlock": true,
        "AirlineBlock": true,
        "TourBooking": true,
        "TourBookingApproval": true,
        "TemporaryTourBookings": true,
        "HotelVoucher": true,
        "MultiHotelVoucher": true,
        "RouteVoucher": true,
        "MultiRouteVoucher": true,
        "AirlineVoucher": true,
        "MultiAirlineVoucher": true,
        "CreditNotes": true,
        "SelfTicket": true,
        "PaymentReceipt": true,
        "CashReceiptPendingApproval": true,
        "BankReceiptPendingApproval": true,
        "HotelBlockReport": true,
        "SectorWithPaymentReport": true,
        "SectorWithoutPaymentReport": true,
        "CompanyWiseBookingReport": true,
        "PendingPaymentReport": true,
        "HotelReport": true,
        "RouteReport": true,
        "CashRegisterReport": true,
        "BankRegisterReport": true,
        "LTCReport": true,
        "CashBalanceManagementReport": true,
        "GSTReport": true,
        "CreditNoteReport": true,
        "SectorWiseBankDetails": true,
        "HotelBlockRoomStatus": true,
        "TourWisePendingPaymentReport": true,
        "UserWiseTourBookingReport": true,
        "PendingHotelVoucherReport": true,
        "PendingRouteVoucherReport": true,
        "Credit": true,
        "Debit": true,
        "Accounts": true,
        "UpdateAccounts": true,
        "OtherBranchReceipt": true,
        "AccountReport": true,
        "SpecialityType": true,
        "Region": true,
        "Country": true,
        "State": true,
        "City": true,
        "Sector": true,
        "SubSector": true,
        "VehicleSharingPax": true,
        "Branch": true,
        "PaxType": true,
        "ManageTours": true,
        "MealType": true,
        "Airport": true,
        "Airline": true,
        "AirlineClass": true,
        "PNRFrom": true,
        "FlightPlace": true,
        "VehicleType": true,
        "Route": true,
        "RouteMapping": true,
        "VendorType": true,
        "Vendor": true,
        "HotelMaster": true,
        "HotelMapping": true,
        "RoomType": true,
        "ExpenseType": true,
        "ExpenseTypeDetails": true,
        "Company": true,
        "Bank": true,
        "Employee": true,
        "Currency": true,
        "BranchWiseTourBookingSummary": true,
        "UserWiseTourBookingSummary": true,
        "RouteReportByTransporter": true,
        "BoardCastMessages": true,
        "UserTypes": true,
        "TourBookingView": true,
        "CreditNoteReceiptApproval": true,
        "InfozealBooking": true,
        "RoomCategory": true,
        "SelfTourBookingReport": true,
        "SelfPendingPaymentReport": true,
        "PlaceWiseHotelReport": true,
        "VehicleAllotmentReport": true,
        "HotelReport2022": true
    }*/
