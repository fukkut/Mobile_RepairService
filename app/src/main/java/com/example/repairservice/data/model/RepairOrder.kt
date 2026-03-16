package com.example.repairservice.data.model

data class RepairOrder(
    val id: Int,
    var deviceName: String,
    var deviceType: String,
    var problemDescription: String,
    var workDone: String,
    var price: Double,
    var status: RepairStatus,
    var clientId: Int,
    val createdDate: String
)