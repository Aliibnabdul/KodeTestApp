package com.example.koder.domain

sealed class UiModel {
    data class EmployeeItem(val employee: EmployeeDomainModel) : UiModel()
    data class SeparatorItem(val description: String) : UiModel()
}