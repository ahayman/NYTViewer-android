package com.example.nytviewer.services.transport.models

enum class PeriodRequest(val period: Int) {
    Day(1),
    Week(7),
    Month(30),
}