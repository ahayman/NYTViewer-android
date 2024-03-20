package com.example.nytviewer.services.transport.utils

/**
 * Network Requests will either succeed and produce the properly decoded type,
 * or they will fail with an error message and an optional code (if it's an HTTP error).
 * This class represents those outcomes.
 */
sealed class TransportResponse<out T> {
    /**
     * Represents a successful network operation.
     * @param T The type of the data returned.
     * @param data The data returned from the network operation.
     */
    data class Success<out T>(val data: T) : TransportResponse<T>()

    /**
     * Represents a failed network operation.
     * @param error The error returned from the network operation, null by default.
     */
    data class Error(val code: Int? = null, val message: String) : TransportResponse<Nothing>() {
        val errorMessage: String
            get() = code?.let{ "($it) $message" } ?: message
    }
}
