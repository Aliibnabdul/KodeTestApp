package com.example.koder.data

import com.example.koder.data.network.ApiInterface
import com.example.koder.domain.ApiResult
import com.example.koder.domain.ApplicationError
import com.example.koder.domain.toEmployeeDomainModel
import com.example.koder.domain.toError
import java.net.UnknownHostException

class KoderRepositoryImpl(private val apiInterface: ApiInterface) : KoderRepository {
    override suspend fun getEmployees(): ApiResult {
        return try {
            ApiResult.Success(apiInterface.getEmployees().items.map { it.toEmployeeDomainModel() })
        } catch (exception: UnknownHostException) {
            ApplicationError.NoInternetConnection.toError()
        } catch (exception: Exception) {
            ApplicationError.Generic.toError()
        }
    }
}