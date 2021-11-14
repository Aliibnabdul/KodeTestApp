package com.example.koder.data

import com.example.koder.domain.ApiResult

interface KoderRepository {
    suspend fun getEmployees(): ApiResult
}