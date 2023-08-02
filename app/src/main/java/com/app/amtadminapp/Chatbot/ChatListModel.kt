package com.app.amtadminapp.Chatbot

data class ChatListModel (
    val Uid : String? = "",
    val device_token : String? = "",
    val id: Int? = 0,
    val image: String? = "",
    val mobile: String? = "",
    val name : String? = "",
    val usertype : String? = "",
    val state : String? = "",
    val screenstate : String? = ""
    )
data class GroupMemberList (
    val Name: String? = null,
    val Uid: String? = null,
    val Device_Token: String? = null,
    val MobileNo: String? = null
)
data class  GroupList (
    val Name: String? = null,
    val DateTime: String? = null
        )