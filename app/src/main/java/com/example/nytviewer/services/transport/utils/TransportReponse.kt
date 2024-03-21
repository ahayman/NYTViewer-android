package com.example.nytviewer.services.transport.utils

/**
 * Network Requests will either succeed and produce the properly decoded type,
 * or they will fail with an error message and an optional code (if it's an HTTP error).
 * This class represents those outcomes.
 */
sealed class TransportResponse<out T> {
    /**
     * Represents a successful network operation.
     * @param data The data returned from the network operation.
     */
    data class Success<out T>(val data: T) : TransportResponse<T>()

    /**
     * Represents a failed network operation.
     * @param code If the network returned an HTTP code, it should be provided here.
     * @param message The error returned from the network operation
     */
    data class Error(val code: Int? = null, val message: String) : TransportResponse<Nothing>() {
        val errorMessage: String
            get() = code?.let{ "($it) $message" } ?: message
    }
}
