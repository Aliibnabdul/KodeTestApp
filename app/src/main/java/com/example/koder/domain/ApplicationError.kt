package com.example.koder.domain

import com.example.koder.R

sealed class ApplicationError {
    object Generic : ApplicationError()
    object NoInternetConnection : ApplicationError()

    fun message(): Int {
        return when (this) {
            Generic -> R.string.genericErrorMessage
            NoInternetConnection -> R.string.connectionErrorMessage
        }
    }
}