package com.example.nytviewer.ui.theme

import android.app.Activity
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.example.nytviewer.ui.common.CustomRippleTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Protocol for the ThemeProvider. It must provide a StateFlow object
 * for each of the main theme components (ex: colors, font). Updates are
 * then collected in MaterialTheme Composable for display.
 *
 * StateFlow is used to update the colors/font in real time.
 */
interface ThemeProvider {
    val colors: StateFlow<ColorTheme>
    val font: StateFlow<FontDef>
}

/**
 * Interface extends the ThemeProvider to allow updating thematic items.
 * This is purely a convenience interface, and is not required for the Theme system
 * to work. The Theme system only requires a ThemeProvider.
 */
interface MutableThemeProvider : ThemeProvider {
    fun setColors(colors: ColorTheme)
    fun setFont(font: FontDef)
}

/**
 * ThemeProviders are small, concrete objects that act as a ThemeProvider in limited situations.
 * They're intended to be used in situations like Testing and Previews, where we need to easily
 * construct a Provider without having to instantiate a class.
 */
object ThemeProviders {
    /**
     * A Static Provider is a non-editable provider with reasonable defaults.
     */
    private fun staticProvider(
        colors: ColorTheme = ColorTheme.System,
        font: FontDef = FontDef.System
    ): ThemeProvider {
        return object : ThemeProvider {
            override val colors = MutableStateFlow(colors)
            override val font = MutableStateFlow(font)
        }
    }

    /**
     * Static light & dark providers.
     * Used primarily in Previews.
     */
    var darkProvider = staticProvider(ColorTheme.Dark)
    var lightProvider = staticProvider(ColorTheme.Light)

    /**
     * The inMemory provider provides an editable provider that will not
     * persist it's state in any form. Any edits or changes to the provider will be lost
     * when the app is removed from memory.
     */
    fun inMemoryProvider(
        colors: ColorTheme = ColorTheme.System,
        font: FontDef = FontDef.System
    ): MutableThemeProvider {
        return object : MutableThemeProvider {
            override val colors = MutableStateFlow(colors)
            override val font = MutableStateFlow(font)

            override fun setColors(colors: ColorTheme) {
                this.colors.value = colors
            }

            override fun setFont(font: FontDef) {
                this.font.value = font
            }
        }
    }
}

/**
 * The Primary Theme provider. This Composable should wrap around any/all Composables
 * that need access to a theme. Normally, this would be done in `MainActivity` under
 * `setContent`.
 *
 * Requires a `provider: ThemeProvider` which determines the theme being used. Updates/changes
 * to the Theme should occur through the ThemeProvider.
 *
 * Requires `content`, which is the Composable that is being wrapped.
 */
@Composable
fun NYTViewerTheme(
    provider: ThemeProvider,
    content: @Composable () -> Unit
) {
    val colorTheme by provider.colors.collectAsState()
    val colors = colorTheme.colors()
    val font by provider.font.collectAsState()
    val view = LocalView.current
    val isDark = colorTheme.isDarkTheme()
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colors.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = isDark
        }
    }

    CompositionLocalProvider(
        LocalRippleTheme provides CustomRippleTheme(
            MaterialTheme.colorScheme.inversePrimary,
            colorTheme.isDarkTheme()
        )
    ) {
        MaterialTheme(
            colorScheme = colors,
            typography = font.typography,
            content = content
        )
    }
}
