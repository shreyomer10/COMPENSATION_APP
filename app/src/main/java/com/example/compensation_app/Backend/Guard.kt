package com.example.compensation_app.Backend

data class Guard(
    val emp_id: String,
    val name: String,
    val mobile_number: String,
    val division: String,
    val range_: String,
    val beat: Int
)
data class VerifyGuardRequest(
    val emp_id: String,
    val mobile_number: String
)

data class VerifyGuardResponse(
    val message: String,
    val employee: Guard? // Use the Guard class to represent employee details if applicable
)