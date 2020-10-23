package com.rabitech.dataModels

import java.util.*

data class Harvest_data_model (
     val user: String = "",

     val constituency: String = "",
     val ward: String = "",
     val location: String = "",

     val farm_size:String = "",
     val imageurl: String = "",
     val plantDate: Date = Date()
)