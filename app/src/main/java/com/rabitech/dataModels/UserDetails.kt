package com.rabitech.dataModels

import com.google.firebase.Timestamp

data class UserDetails(
    val user_fname: String = "",
    val user_lname: String = "",
    val user_phone: String = "",
    val user_email: String = "",
    val user_National_id: String = "",
//    @ServerTimestamp var date_time_reported: Timestamp? = null,
    val time_request_sent: Timestamp? = null,
    val status_of_request:Boolean = false
)
