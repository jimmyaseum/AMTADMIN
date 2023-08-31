package com.app.amtadminapp.model.response.booking

import com.google.android.material.textfield.TextInputLayout
import com.google.gson.annotations.SerializedName

data class PaxDataModel (

    @SerializedName("paxid")
    var paxid: Int = 0,

    @SerializedName("pax")
    var pax: String = "",

    @SerializedName("noofroom")
    var noofroom: String = "",

    //TIL Error handling
    @SerializedName("tilPaxType")
    var tilPaxType: TextInputLayout? = null,

    @SerializedName("tilNoofRooms")
    var tilNoofRooms: TextInputLayout? = null


    )

)

