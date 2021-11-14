package com.example.koder.domain

sealed class ApplicationError {

    data class Generic(val cause: Throwable? = null) : ApplicationError()
    object NoInternetConnection : ApplicationError()

}