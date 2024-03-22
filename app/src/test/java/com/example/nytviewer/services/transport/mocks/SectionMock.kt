package com.example.nytviewer.services.transport.mocks

import com.example.nytviewer.services.transport.models.Section

fun createSection(idx: Int) = Section("section_$idx", "Section $idx")