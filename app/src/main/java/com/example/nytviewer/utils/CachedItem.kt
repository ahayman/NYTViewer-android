package com.example.nytviewer.utils

import java.util.Date

/**
 * Defined an expiration from a specific interval from "now". (Ex: 30 seconds, 10 minutes, etc)
 * Will return true from [isExpired] if the current date is after the expiry date.
 */
sealed class CacheExpiry(val expiryDate: Date) {
    data class Seconds(val interval: Int) : CacheExpiry(Date(Date().time + 1000 * interval))
    data class Minutes(val interval: Int) : CacheExpiry(Date(Date().time + (1000 * 60 * interval)))
    data class Hours(val interval: Int) : CacheExpiry(Date(Date().time + (1000 * 60 * 60 * interval)))
    data class Days(val interval: Int) : CacheExpiry(Date(Date().time + (1000 * 60 * 60 * 24 * interval)))

    val isExpired: Boolean
        get() = Date() >= expiryDate
}

/**
 * A simple caching mechanism that hold a value along with an expiry definition.
 * If the item is expired, the [isExpired] property will return [true].
 * This will not automatically eject expired items.
 */
data class CachedItem<T>(
    val item: T,
    val expiry: CacheExpiry
) {
   val isExpired: Boolean
       get() = expiry.isExpired
}