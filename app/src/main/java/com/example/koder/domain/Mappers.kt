package com.example.koder.domain

import com.example.koder.data.network.EmployeeDto
import com.example.koder.extensions.toLocalDate

fun EmployeeDto.toUiModel() = EmployeeUiModel(
    id = id,
    avatarUrl = avatarUrl,
    firstName = firstName,
    lastName = lastName,
    userTag = userTag,
    department = department.toUiDepartment(),
    position = position,
    birthday = birthday,
    birthdayLocalDate = birthday.toLocalDate(),
    phone = phone
)

private fun String.toUiDepartment(): String{
    return when(this){
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