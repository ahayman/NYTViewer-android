package com.example.nytviewer.services.transport.utils

/**
 * Data needed by the TransportService to work properly.
 */
interface TransportKeysProvider {
    /// The api key for a registered developer account with the NYTimes
    val apiKey: String
    /// The baseURl to retrieve data from.
    val baseUrl: String
}