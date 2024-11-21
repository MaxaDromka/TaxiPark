package com.example.taxipark

data class Driver(
    val driverID: Int,
    val name: String,
    val licenseNumber: String,
    val phoneNumber: String,
    val rating: Double
)