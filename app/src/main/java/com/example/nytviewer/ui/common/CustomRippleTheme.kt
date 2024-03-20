package com.example.nytviewer.ui.common

import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Designed to be modify the Ripple Theme used by Buttons and Touch Components to
 * match the Theme used. This generally should only be used once to cover all components,
 * but this can be used in a LocalComposition to modify the RippleTheme if required.
 */
class CustomRippleTheme(val ripple: Color, val forDarkTheme: Boolean) : RippleTheme {
    @Composable
    override fun defaultColor() = ripple

    @Composable
    override fun rippleAlpha(): RippleAlpha =
        RippleTheme.defaultRippleAlpha(contentColor = ripple, lightTheme = !forDarkTheme)
}

