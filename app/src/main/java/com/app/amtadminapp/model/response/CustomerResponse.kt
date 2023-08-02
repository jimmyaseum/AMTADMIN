package com.app.amtadminapp.model.response

import com.google.gson.annotations.SerializedName

data class CustomerResponse(
    @SerializedName("Data")
    val Data: CustomerListModel? = null,
    @SerializedName("Details")
    val Details: Any?,
    @SerializedName("Message")
    val Message: String,
    @SerializedName("Status")
    val Status: Int
)

data class CustomerListModel (
    //general information
    @SerializedName("ID")
    val ID: Int = 0,
    @SerializedName("Initials")
    val Initials: String = "",
    @SerializedName("FirstName")
    val FirstName: String? = "",
    @SerializedName("LastName")
    val LastName: String? = "",
    @SerializedName("MobileNo")
    val MobileNo: String = "",
    @SerializedName("ParentCustomerID")
    val ParentCustomerID: Int = 0,
    @SerializedName("RelationshipID")
    val RelationshipID: Int = 0,
    @SerializedName("Relation")
    val Relation: String = "",
    @SerializedName("Address")
    val Address: String = "",
    @SerializedName("EmailID")
    val EmailID: String = "",
    @SerializedName("ResidentPhoneNo")
    val ResidentPhoneNo: String = "",
    @SerializedName("TravellingMobileNo")
    val TravellingMobileNo: String = "",
    @SerializedName("EmergencyNo")
    val EmergencyNo: String = "",
    @SerializedName("DOB")
    val DOB: String? = null,
    @SerializedName("Gender")
    val Gender: String = "",
    @SerializedName("CityID")
    val CityID: Int = 0,
    @SerializedName("CityName")
    val CityName: String = "",
    @SerializedName("StateID")
    val StateID: Int = 0,
    @SerializedName("StateName")
    val StateName: String,
    @SerializedName("CountryID")
    val CountryID: Int = 0,
    @SerializedName("CountryName")
    val CountryName: String,
    @SerializedName("Pincode")
    val Pincode: String = "",
    @SerializedName("CustomerImage")
    val CustomerImage: String = "",

    //company information
    @SerializedName("CompanyName")
    val CompanyName: String = "",
    @SerializedName("CompanyAddress")
    val CompanyAddress: String = "",
    @SerializedName("CompanyMobileNo")
    val CompanyMobileNo: String = "",
    @SerializedName("CompanyEmailID")
    val CompanyEmailID: String = "",
    @SerializedName("CompanyGSTNo")
    val CompanyGSTNo: String = "",
    @SerializedName("CompanyPanNo")
    val CompanyPanNo: String = "",
    @SerializedName("CompanyGSTCopy")
    val CompanyGSTCopy: String = "",
    @SerializedName("CompanyPanCopy")
    val CompanyPanCopy: String = "",
    @SerializedName("CompanyPincode")
    val CompanyPincode: String = "",
    @SerializedName("CompanyCityID")
    val CompanyCityID: Int = 0,
    @SerializedName("CompanyCityName")
    val CompanyCityName: String = "",
    @SerializedName("CompanyStateID")
    val CompanyStateID: Int = 0,
    @SerializedName("CompanyStateName")
    val CompanyStateName: String = "",
    @SerializedName("CompanyCountryID")
    val CompanyCountryID: Int = 0,
    @SerializedName("CompanyCountryName")
    val CompanyCountryName: String = ""
    )
