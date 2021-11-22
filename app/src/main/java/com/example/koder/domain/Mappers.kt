package com.example.koder.domain

import com.example.koder.data.network.EmployeeDto
import com.example.koder.extensions.toLocalDate
import com.example.koder.extensions.toNextLocalDate
import com.example.koder.extensions.toUiFormat

fun EmployeeDto.toEmployeeDomainModel() = EmployeeDomainModel(
    id = id,
    avatarUrl = avatarUrl,
    firstName = firstName,
    lastName = lastName,
    userTag = userTag.lowercase(),
    department = department.toUiDepartment(),
    position = position,
    birthday = birthday.toUiFormat(),
    birthdayLocalDate = birthday.toLocalDate(),
    nextBirthdayLocalDate = birthday.toNextLocalDate(),
    phone = phone
)

private fun String.toUiDepartment(): String {
    return when (this) {
        "android" -> "Android"
        "ios" -> "iOS"
        "design" -> "Дизайн"
        "management" -> "Менеджмент"
        "qa" -> "QA"
        "back_office" -> "Бэк-офис"
        "frontend" -> "Frontend"
        "hr" -> "HR"
        "pr" -> "PR"
        "backend" -> "Backend"
        "support" -> "Техподдержка"
        "analytics" -> "Аналитика"
        else -> "Все"
    }
}