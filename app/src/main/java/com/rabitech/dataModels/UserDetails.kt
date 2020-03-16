package com.rabitech.dataModels

data class UserDetails
    (
    val user_fname: String = "",
    val user_lname: String = "",
    val user_phone: String = "",
    val user_email: String = "",
    val user_National_id: String = "",
    val time_request_sent:String ="",
    val status_of_request:String = "Awaiting Approval"
)
