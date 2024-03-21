package com.example.nytviewer.ui.theme

import android.content.Context
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.nytviewer.utils.isDarkMode

/**
 * Color Theme defines the Colors that can be used in the app.
 * There are a variety of sealed objects that represent pre-set color themes.
 * In addition, the `Custom` theme can be used to create a Custom color set to be used.
 */
sealed class ColorTheme {
    data object System : ColorTheme()
    data object Light : ColorTheme()
    data object Dark : ColorTheme()
    data class Custom(
        val schemeName: String,
        val lightColorScheme: ColorScheme,
        val darkColorScheme: ColorScheme
    ) : ColorTheme()

    companion object {
        val presetThemes: List<ColorTheme>
            get() = listOf(System, Light, Dark)
    }

    /**
     * Primary method of retrieving the colors for this theme.
     * Can only be used within a Composable function.
     */
    @Composable
    fun colors(): ColorScheme {
        return when (this) {
            System -> if (isSystemInDarkTheme()) DarkTheme else LightTheme
            Light -> LightTheme
            Dark -> DarkTheme
            is Custom -> if (isSystemInDarkTheme()) this.darkColorScheme else this.lightColorScheme
        }
    }

    val name: String
        @Composable
        get() = when (this) {
            System -> if (isSystemInDarkTheme()) "System (Dark)" else "System (Light)"
            Light -> "Light"
            Dark -> "Dark"
            is Custom -> this.schemeName
        }

    /**
     * A secondary method of retrieving colors for a theme.
     * Used when the theme must be accessed outside of a composable.
     * Must provide a context (used to determine dark/light mode).
     */
    fun colors(context: Context): ColorScheme {
        return when (this) {
            System -> if (context.isDarkMode()) DarkTheme else LightTheme
            Light -> LightTheme
            Dark -> DarkTheme
            is Custom -> if (context.isDarkMode()) this.darkColorScheme else this.lightColorScheme
        }
    }

    @Composable
    fun isDarkTheme(): Boolean = when(this) {
        System -> isSystemInDarkTheme()
        is Custom -> isSystemInDarkTheme()
        Dark -> true
        Light -> false
    }
}

/**
 * ================ Preset Color Themes ===================
 */

/**
 * A simple light colored theme, primarily intended for use when the app is in light mode.
 */
private val LightTheme = lightColorScheme(
    primary = Color(0xFF003c71),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFD4E3FF),
    onPrimaryContainer = Color(0xFF001C3A),
    secondary = Color(0xFF0065BE),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFD5E3FF),
    onSecondaryContainer = Color(0xFF001B3B),
    tertiary = Color(0xFF009104),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFF83FD6F),
    onTertiaryContainer = Color(0xFF002200),
    error = Color(0xFFBA1A1A),
    errorContainer = Color(0xFFFFDAD6),
    onError = Color(0xFFFFFFFF),
    onErrorContainer = Color(0xFF410002),
    background = Color(0xFFFFFFFF),
    onBackground = Color(0xFF1A1C1E),
    outline = Color(0xFF74777F),
    inverseOnSurface = Color(0xFFF1F0F4),
    inverseSurface = Color(0xFF2F3033),
    inversePrimary = Color(0xFFA5C8FF),
    surfaceTint = Color(0xFF003c71),
    outlineVariant = Color(0xFFC3C6CF),
    scrim = Color(0x22000000),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF1A1C1E),
    surfaceVariant = Color(0xFFE0E2EB),
    onSurfaceVariant = Color(0xFF757575)
)

/**
 * A simple dark colored theme, primarily intended for use when the app is in dark mode.
 */
private val DarkTheme = darkColorScheme(
    primary = Color(0xFFA5C8FF),
    onPrimary = Color(0xFF00315E),
    primaryContainer = Color(0xFF004785),
    onPrimaryContainer = Color(0xFFD4E3FF),
    secondary = Color(0xFFA7C8FF),
    onSecondary = Color(0xFF003060),
    secondaryContainer = Color(0xFF004788),
    onSecondaryContainer = Color(0xFFD5E3FF),
    tertiary = Color(0xFF67E055),
    onTertiary = Color(0xFF003A00),
    tertiaryContainer = Color(0xFF005301),
    onTertiaryContainer = Color(0xFF83FD6F),
    error = Color(0xFFFFB4AB),
    errorContainer = Color(0xFF93000A),
    onError = Color(0xFF690005),
    onErrorContainer = Color(0xFFFFDAD6),
    background = Color(0xFF1A1C1E),
    onBackground = Color(0xFFE3E2E6),
    outline = Color(0xFF8D9199),
    inverseOnSurface = Color(0xFF1A1C1E),
    inverseSurface = Color(0xFFE3E2E6),
    inversePrimary = Color(0xFF003c71),
    surfaceTint = Color(0xFFA5C8FF),
    outlineVariant = Color(0xFF43474E),
    scrim = Color(0xFF000000),
    surface = Color(0xFF121316),
    onSurface = Color(0xFFC6C6CA),
    surfaceVariant = Color(0xFF43474E),
    onSurfaceVariant = Color(0xFFC3C6CF)
)

