package com.example.nytviewer.services.transport.models

/**
 * Shared Articles must request a period over which the articles were popular.
 * This defines all allowable periods.
 */
enum class PeriodRequest(val period: Int) {
    Day(1),
    Week(7),
    Month(30),
}