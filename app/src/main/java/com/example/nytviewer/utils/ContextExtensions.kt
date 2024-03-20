package com.example.nytviewer.utils

import android.content.Context
import android.content.res.Configuration

/**
 * A convenience method to determine if a particular context is in Dark mode.
 * A lot easier calling this than trying to remember what the masks/values are.
 */
fun Context.isDarkMode(): Boolean {
    return resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
}
