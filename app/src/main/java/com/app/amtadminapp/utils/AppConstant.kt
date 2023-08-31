package com.app.amtadminapp.utils

object AppConstant {

    var TOKEN : String = ""

    val BASE_URL_APP = "http://38.17.55.183/amtapp2022/api/" //  AI005 app development purpose Test
    val BASE_URL_WEB = "http://38.17.55.183/amtapi2022/api/" //  AI005 web development purpose Test

//
//    val BASE_URL_APP = "http://amtapp.ajaymoditravels.com/api/" //  AI005 development purpose
//    val BASE_URL_WEB = "https://amtcrm.ajaymoditravels.com/api/" //  AI005 development purpose

    val DEVICETYPE = 1

    const val TOAST_LONG = 1
    const val TOAST_SHORT = 0
    val IS_API_CALL = "IS_API_CALL"
    const val BLUR_RADIUS = 35
    internal val PREF_NAME = "amtadmin_pref"

    //Date format
    val YYYY_MM_dd_Slash: String = "yyyy/MM/dd"
    val yyyy_MM_dd_Dash: String = "yyyy-MM-dd"
    val dd_MM_yyyy_Slash: String = "dd/MM/yyyy"
    val ddMMyyyy_HHmmss : String = "dd/MM/yyyy HH:mm:ss"
    const val DD_MMM_YYYY_FORMAT = "MMMM, dd yyyy"
    const val ISO_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    const val HH_MM_AA_FORMAT = "hh:mm aa" //04:30 PM
    const val HH_MM_FORMAT = "HH:mm" //16:30
    const val day_d_MM_YYYY_HH_mm_ss_z_FORMAT = "EEE, d MMM yyyy HH:mm:ss Z"
    const val day_d_MM = "EEE, d MMM"
    const val day_d_MM_YYYY = "EEE, d MMM yyyy"
    //Date comparision
    const val BEFORE = "BEFORE"
    const val AFTER = "AFTER"
    const val EQUAL = "EQUAL"

//    var SECTORID = 2
//    var TOURID = 1
//    var TOURDATE = "2022-02-14"
//    var TOURBookingID = 136
//    var CustomerID = 68

    var TOURBookingID = 0
    var BookingNo = ""
    var CustomerID = 0
    var Travel_Type = ""

    var TOURBookingNO = ""
    var HotelVoucherIDs = 0

    val SpecialityFilters = "SpecialityFilters"
    val DurationFilters = "DurationFilters"
    val BudgetFilters = "BudgetFilters"
}