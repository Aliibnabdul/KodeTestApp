package com.example.koder.domain

import java.time.LocalDate

data class EmployeeUiModel(
    val id: String,
    val avatarUrl: String,
    val firstName: String,
    val lastName: String,
    val userTag: String,
    val department: String,
    val position: String,
    val birthday: String,
    val birthdayLocalDate: LocalDate,
    val phone: String
)
