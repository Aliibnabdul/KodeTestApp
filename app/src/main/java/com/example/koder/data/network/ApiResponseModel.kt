package com.example.koder.data.network

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponseModel(
    val items: List<EmployeeDto>
)
