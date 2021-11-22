package com.example.koder.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
data class EmployeeDomainModel(
    val id: String,
    val avatarUrl: String,
    val firstName: String,
    val lastName: String,
    val userTag: String,
    val department: String,
    val position: String,
    val birthday: String,
    val birthdayLocalDate: LocalDate,
    val nextBirthdayLocalDate: LocalDate,
    val phone: String
): Parcelable
