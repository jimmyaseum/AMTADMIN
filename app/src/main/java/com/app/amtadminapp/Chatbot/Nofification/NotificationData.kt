package com.app.amtadminapp.Chatbot.Nofification

data class PushNotification(
    val notification : NotificationData,
    val to : String? = ""
)

data class NotificationData(
    val title : String? = "",
    val body : String? = ""
)

data class PushGroupNotification(
    val notification : NotificationData,
    val operation : String? = "",
    val notification_key_name: String? = "",
    val registration_ids: ArrayList<String>? = null
)