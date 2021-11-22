package com.example.koder.domain

sealed class ApiResult {
    data class Success(val result: List<EmployeeDomainModel>) : ApiResult()
    data class Error(val error: ApplicationError) : ApiResult()
}

fun ApplicationError.toError() = ApiResult.Error(this)

