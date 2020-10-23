package com.rabitech.dataModels

data class HarvestRequest(
    val locationDetails: LocationDetails? = null,
    val farmDetails: FarmDetails? = null,
    val farmImage: String = "",
    val status : String = "",
    val userId: String = "",
    var requestId :String = ""

)

data class LocationDetails(
    val constituency: String = "",
    val ward: String = "",
    val landMark: String = "",
)

data class FarmDetails(
    val size: Int = 0,
    val datePlanted: String = ""
)
