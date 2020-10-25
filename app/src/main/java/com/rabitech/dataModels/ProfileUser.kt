package com.rabitech.dataModels

import com.google.firebase.Timestamp

data class ProfileUser(
    val firstName: String = "",
    val lastName: String = "",
    val phone: String = "",
    val email: String = "",
    val nationalId: String = "",
    val userId:String = "",

//    val time_request_sent: Timestamp? = null,
//    val status_of_request:Boolean = false
)
