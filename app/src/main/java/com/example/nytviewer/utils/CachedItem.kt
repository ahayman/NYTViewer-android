package com.example.nytviewer.utils

import java.util.Date

sealed class CacheExpiry(val expiryDate: Date) {
    data class Seconds(val interval: Int) : CacheExpiry(Date(Date().time + 1000 * interval))
    data class Minutes(val interval: Int) : CacheExpiry(Date(Date().time + (1000 * 60 * interval)))
    data class Hours(val interval: Int) : CacheExpiry(Date(Date().time + (1000 * 60 * 60 * interval)))
    data class Days(val interval: Int) : CacheExpiry(Date(Date().time + (1000 * 60 * 60 * 24 * interval)))

    val isExpired: Boolean
        get() = Date() >= expiryDate
}

data class CachedItem<T>(
    val item: T,
    val expiry: CacheExpiry
) {
   val isExpired: Boolean
       get() = expiry.isExpired
}