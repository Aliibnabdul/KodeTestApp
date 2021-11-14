package com.example.koder.data.network

import kotlinx.serialization.Serializable

@Serializable
data class EmployeeDto(
    val id: String,
    val avatarUrl: String,
    val firstName: String,
    val lastName: String,
    val userTag: String,
    val department: String,
    val position: String,
    val birthday: String,
    val phone: String
)
