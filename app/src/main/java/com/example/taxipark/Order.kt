package com.example.taxipark

data class Order(
    val orderID: Int,
    val driverID: Int,
    val vehicleID: Int,
    val pickupLocation: String,
    val dropoffLocation: String,
    val status: String
)