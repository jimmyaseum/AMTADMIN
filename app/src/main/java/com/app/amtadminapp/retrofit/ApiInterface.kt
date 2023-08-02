package com.app.amtadminapp.retrofit

import com.app.amtadminapp.Chatbot.Nofification.PushGroupNotification
import com.app.amtadminapp.Chatbot.Nofification.PushNotification
import com.app.amtadminapp.model.AdminAppVersion
import com.app.amtadminapp.model.TourBookingDetailsResponse
import com.app.amtadminapp.model.TourBookingPaxResponse
import com.app.amtadminapp.model.response.*
import com.app.amtadminapp.model.response.booking.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import java.util.*

/**
 * Created by Jimmy
 */
interface ApiInterface {

    // region using web api

    @POST("Customers/Resigtartion")
    fun registration(@Body body: RequestBody?): Call<RegistrationResponse>

    @POST("Login/logout")
    fun logout(@Body body: RequestBody?): Call<CommonResponse>

    @POST("Login/Login")
    fun login(@Body body: RequestBody?): Call<LoginResponse>

    @POST("Login/OTPVerification")
    fun VerifyOTP(@Body body: RequestBody?): Call<RegistrationResponse>

    @GET("destination/findallactive")
    fun getAllSector(): Call<SectorListResponse>

    @POST("subsector/SubSectorListBySectorID")
    fun getAllSubSector(@Body body: RequestBody?): Call<SubSectorListResponse>

    @POST("Tour/TourBySectorID")
    fun getAllTourPackage(@Body body: RequestBody?): Call<TourPackageListResponse>

    @GET("tour/tourlist")
    fun getAllTour(): Call<TourPackageListResponse>

    @GET("mealtypes/findallactive")
    fun getAllMeal(): Call<MealListResponse>

    @GET("RoomCategory/FindAllActive")
    fun getAllRoomCategory(): Call<RoomCategoryListResponse>

    @GET("company/findallactive")
    fun getAllCompany(): Call<CompanyListResponse>

    @GET("Employee/FindAllActive")
    fun getAllEmployee(): Call<EmployeeListResponse>

    @GET("branch/findall")
    fun getAllBranch(): Call<BranchListResponse>

    @GET("TourDates/FindByTourID")
    fun getTourDates(@Query("TourID") _id: Int): Call<TourInfoResponse> // done

    @GET("paxtype/findallactive")
    fun getAllPaxType(): Call<PaxTypeListResponse>

    @GET("specialitytype/findall")
    fun getAllSpecialityType(): Call<SpecialityTypeListResponse>

    @GET("VehicleSharingPax/FindAllActive")
    fun getAllVehicleSharingPax(): Call<VehicleSharingListResponse>

    @GET("roomtypes/findallactive")
    fun getAllRoomTypes(): Call<RoomTypeListResponse>

    @POST("city/citybysectorid")
    fun getAllCityBySector(@Body body: RequestBody?): Call<AllCityListResponse>

    @POST("tourflightcost/gettourcost")
    fun GetTourCost(@Body body: RequestBody?): Call<TourCostResponse>

    @POST("tourplace/TourPlaceByTourIDDate")
    fun GetTourPlaceDate(@Body body: RequestBody?): Call<TourPlaceDateResponse>

    @GET("Hotels/HotelByCityID")
    fun getHotelByPlace(@Query("CityID") _id: Int): Call<HotelListResponse>

    @POST("dashboard/dashboard")
    fun GetDashBoard(@Body body: RequestBody?): Call<DashBoardResponse>

    @GET("TourBooking/FindByID")
    fun getTourBookingInfo(@Query("id") _id: Int): Call<TourBookingInfoResponse> // done

    @GET("vendor/findallActive")
    fun getAllVender(): Call<VenderListResponse>

    @POST("TourBooking/SaveTourInformation") // step 5
    fun AddTourOtherInformation(@Body body: RequestBody?): Call<CommonResponse>

    @POST("otp/generateotp") // step 5
    fun SendPTP(@Body body: RequestBody?): Call<CommonResponse>

    @POST("tourbooking/insertremarks") // step 6
    fun InserRemarks(@Body body: RequestBody?): Call<CommonResponse>

    @POST("tourbooking/remarksbybookingid") // step 6
    fun GetRemarkList(@Body body: RequestBody?): Call<RemarksListResponse>


    @POST("TourBooking/MultiTourBookingView")
    fun GetMultiTourBookingView(@Query("PBNNo") _id: Int): Call<MultiTourBookingViewResponse>

    @GET("TourBooking/FindByID") // add paging
    fun GetTourBookingView(@Query("id") _id: Int): Call<TourBookingViewResponse>

    @POST("tourflightcost/getpaxtourcost") // add paging
    fun Getpaxtourcost(@Body body: RequestBody?): Call<PaxTourCostResponse>

    @GET("hotelblockroom/checkblockroom")
    fun Checkblockroom(@Query("tourdatecode") tourdatecode: String): Call<CheckBlockRoomResponse>

    @GET("HotelBooking/FindByID") // add paging
    fun GetHotelBookingFindBy(@Query("id") _id: Int): Call<HotelBookingViewResponse>

    // endregion

//--------------------------------------------------------------------------------------

    // region using app api

    @POST("Login/VersionApi")
    fun getAdminAppVersion(@Body body: RequestBody?): Call<AdminAppVersion>

    @GET("City/FindAll")
    fun getAllCity(): Call<CityResponse>

    @POST("TourBooking/Insert") // step 1
    fun AddTourBooking(@Body body: RequestBody?): Call<CommonResponse2>

    @POST("TourBooking/Update") // step 1
    fun UpdateTourBooking(@Body body: RequestBody?): Call<CommonResponse2>

    @POST("TourBooking/TourPlaceBooking") // step 2
    fun AddTourBookingPlace(@Body body: RequestBody?): Call<CommonResponse>

    @POST("TourBooking/TourPersonalInformation") // step 3
    fun AddTourPersonalInformation(@Body body: RequestBody?): Call<CommonResponse>

    @POST("dashboard/bookingdashboard") // dashboard 1
    fun GetDashboardBookingList(@Body body: RequestBody?): Call<BookingListResponse>

    @POST("dashboard/paymentreceiptdashboard") // dashboard 2
    fun GetDashboardPaymentList(@Body body: RequestBody?): Call<PaymentListResponse>

    @POST("dashboard/routevoucherdashboard") // dashboard 3
    fun GetDashboardRouteVoucherList(@Body body: RequestBody?): Call<RouteVoucherListResponse>

    @POST("dashboard/hotelvoucherdashboard") // dashboard 4
    fun GetDashboardHotelVoucherList(@Body body: RequestBody?): Call<HotelVoucherListResponse>

    @POST("dashboard/airlinevoucherdashboard") // dashboard 5
    fun GetDashboardFlightVoucherList(@Body body: RequestBody?): Call<FlightVoucherListResponse>

    @POST("dashboard/pendingpaymentof15daysdeparturelist") // dashboard 6
    fun GetDashboardPendingPaymentList(@Body body: RequestBody?): Call<PendingPaymentListResponse>

    @POST("dashboard/next15daysdeparturelist") // dashboard 7
    fun GetDashboardDepartureList(@Body body: RequestBody?): Call<DepartureListResponse>

    @POST("dashboard/PendigFormBookingDashBoard") // dashboard 8
    fun GetDashboardPendingFormList(@Body body: RequestBody?): Call<PendingFormListResponse>

    @POST("TourBooking/TourBookingList")
    fun GetTourBookingList(@Body body: RequestBody?): Call<TourBookingListResponse>

    @POST("TourBooking/TouristPaxInfo")
    fun UpdateTourPax(@Body body: RequestBody?): Call<CommonResponse>

    @POST("Voucher/HotelVoucherForm1") // step 1
    fun AddHotelVoucherStep1(@Body body: RequestBody?): Call<CommonResponse2>

    @POST("Notification/FindAll")
    fun getNotificationList(@Body body: RequestBody?): Call<NotificationListResponse>

    @POST("Voucher/HotelVoucherFindAll") // add paging
    fun GetAllHotelVoucherList(@Body body: RequestBody?): Call<HotelVoucherResponse>

    @POST("Voucher/RouteVoucherFindAll") // add paging
    fun GetAllRouteVoucherList(@Body body: RequestBody?): Call<RouteVoucherResponse>

    @POST("Voucher/AirlineVoucherFindAll") // add paging
    fun GetAllAirlineVoucherList(@Body body: RequestBody?): Call<AirlineVoucherResponse>

    @Multipart
    @POST("Voucher/AirlineVoucherInsert") // done
    fun AddAirlineVoucher(
                     @Part("TourBookingNo") Note: RequestBody?,
                     @Part("CompanyID") CompanyID: RequestBody,
                     @Part("TicketPurchasedDate") TicketPurchasedDate: RequestBody?,
                     @Part("TotalPrice") TotalPrice: RequestBody?,
                     @Part("CreatedBy") CreatedBy: RequestBody?,
                     @Part("DeparturePNRNo") DeparturePNRNo: RequestBody?,
                     @Part("ArrivalPNRNo") ArrivalPNRNo: RequestBody?,
                     @Part AirlineVoucherTicket: ArrayList<MultipartBody.Part>?
    ): retrofit2.Call<CommonResponse>

    @Multipart
    @POST("Voucher/AirlineVoucherUpdate") // done
    fun UpdateAirlineVoucher(
        @Part("ID") ID: RequestBody?,
        @Part("TourBookingNo") Note: RequestBody?,
        @Part("CompanyID") CompanyID: RequestBody,
        @Part("TicketPurchasedDate") TicketPurchasedDate: RequestBody?,
        @Part("TotalPrice") TotalPrice: RequestBody?,
        @Part("UpdatedBy") CreatedBy: RequestBody?,
        @Part("DeparturePNRNo") DeparturePNRNo: RequestBody?,
        @Part("ArrivalPNRNo") ArrivalPNRNo: RequestBody?,
        @Part AirlineVoucherTicket: ArrayList<MultipartBody.Part>?
    ): retrofit2.Call<CommonResponse>

    @POST("TourBooking/TourBookingPaxDetails")
    fun getTourBookingPaxDetails(@Body body: RequestBody?): Call<TourBookingPaxResponse>


    @POST("Voucher/HotelVoucherViewbyBookingNo")
    fun getTourBookingDetails(@Body body: RequestBody?): Call<TourBookingDetailsResponse>

    @Multipart
    @POST("Voucher/HotelVoucherfromApp") // done
    fun AddHotelVoucher(@Part("CreatedBy") createdby: RequestBody?,
                     @Part("TourBookingNo") TourBookingNo : RequestBody?,
                     @Part("CityIDs") CityIDs : RequestBody?,
                     @Part attachment: ArrayList<MultipartBody.Part>?
    ): retrofit2.Call<CommonResponse>

    @POST("Voucher/HotelVoucherFindByID")
    fun getHotelVoucherFindByID(@Body body: RequestBody?): Call<HotelVoucherFindByIDResponse>

    @Multipart
    @POST("Voucher/HotelVoucherUpdate") // done
    fun UpdateHotelVoucher(@Part("UpdatedBy") UpdatedBy: RequestBody?,
                        @Part("ID") ID : RequestBody?,
                        @Part("CityID") CityID : RequestBody?,
                        @Part attachment: ArrayList<MultipartBody.Part>?
    ): retrofit2.Call<CommonResponse>

    // endregion

//--------------------------------------------------------------------------------------


    @GET("Relation/findall")
    fun getAllRelation(): Call<RelationResponse>

    @GET("Customers/FindByID")
    fun getDetailByCustomers(@Query("id") _id: Int): Call<CustomerResponse> // done

    @Multipart
    @POST("Customers/Insert") // done
    fun CustomerAdd(
        @Query("Initials") Initials: String,
        @Query("FirstName") FirstName: String,
        @Query("LastName") LastName: String,
        @Query("MobileNo") MobileNo: String,
        @Query("ParentCustomerID") ParentCustomerID: Int,
        @Query("RelationshipID") RelationshipID: Int,
        @Query("Address") Address: String,
        @Query("EmailID") EmailID: String,
        @Query("ResidentPhoneNo") ResidentPhoneNo: String,
        @Query("TravellingMobileNo") TravellingMobileNo: String,
        @Query("EmergencyNo") EmergencyNo: String,
        @Query("DOB") DOB: String,
        @Query("Gender") Gender: String,
        @Query("Pincode") Pincode: String,
        @Query("CityID") CityID: Int,
        @Query("StateID") StateID: Int,
        @Query("CountryID") CountryID: Int,
        @Query("IsActive") IsActive: Boolean,
        @Query("CreatedBy") CreatedBy: Int,
        @Part image: ArrayList<MultipartBody.Part>?
    ): Call<CommonResponse>

    @Multipart
    @POST("Customers/Update") // done
    fun CustomerUpdate(
        @Query("ID") ID: Int,
        @Query("Initials") Initials: String,
        @Query("FirstName") FirstName: String,
        @Query("LastName") LastName: String,
        @Query("MobileNo") MobileNo: String,
        @Query("ParentCustomerID") ParentCustomerID: Int,
        @Query("RelationshipID") RelationshipID: Int,
        @Query("Address") Address: String,
        @Query("EmailID") EmailID: String,
        @Query("ResidentPhoneNo") ResidentPhoneNo: String,
        @Query("TravellingMobileNo") TravellingMobileNo: String,
        @Query("EmergencyNo") EmergencyNo: String,
        @Query("DOB") DOB: String,
        @Query("Gender") Gender: String,
        @Query("Pincode") Pincode: String,
        @Query("CityID") CityID: Int,
        @Query("StateID") StateID: Int,
        @Query("CountryID") CountryID: Int,
        @Query("IsActive") IsActive: Boolean,
        @Query("UpdatedBy") CreatedBy: Int,
        @Query("OperationType") OperationType: Int,
        @Part image: ArrayList<MultipartBody.Part>?
    ): Call<CommonResponse>

    // region Send FCM Notification in CHAT
    @Headers("Content-Type:application/json",
        "Authorization:key=AAAAMlaPWSA:APA91bEPIiMadJjbvhWA4hND_5f_Tx_tyCt3HMCpsZuCEO_rHKvG-q6yPbQ7ygvzVlpi3NBANhksegpQLnE6TMEavGVgvYHhC6m-8qTevSxxdSlz1rEUdI_Lsi1c3YPtKhJWtGckqmVs")
    @POST("fcm/send")
    fun sendNotification(@Body notification: PushNotification): Call<PushNotification>

    // region Send FCM Notification in CHAT
    @Headers("Content-Type:application/json",
        "Authorization:key=AAAAMlaPWSA:APA91bEPIiMadJjbvhWA4hND_5f_Tx_tyCt3HMCpsZuCEO_rHKvG-q6yPbQ7ygvzVlpi3NBANhksegpQLnE6TMEavGVgvYHhC6m-8qTevSxxdSlz1rEUdI_Lsi1c3YPtKhJWtGckqmVs")
    @POST("fcm/send")
    fun sendGroupNotification(@Body notification: PushGroupNotification): Call<PushNotification>

}