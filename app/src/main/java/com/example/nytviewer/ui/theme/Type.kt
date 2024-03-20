package com.example.nytviewer.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

/**
 * Defines all the Fonts needed to construct a proper FontFamily.
 * This is entirely for type safety, to ensure every new font we add
 * to the library had all the faces it needs.
 */
data class FamilyFonts(
    val normal: Int,
    val bold: Int,
    val italic: Int,
    val boldItalic: Int,
    val medium: Int,
    val mediumItalic: Int,
    val semiBold: Int,
    val semiBoldItalic: Int,
) {
    val family: FontFamily by lazy {
        FontFamily(
            Font(normal),
            Font(bold, weight = FontWeight.Bold),
            Font(italic, style = FontStyle.Italic),
            Font(boldItalic, weight = FontWeight.Bold, style = FontStyle.Italic),
            Font(medium, weight = FontWeight.Medium),
            Font(mediumItalic, weight = FontWeight.Medium, style = FontStyle.Italic),
            Font(semiBold, weight = FontWeight.SemiBold),
            Font(semiBoldItalic, weight = FontWeight.SemiBold, style = FontStyle.Italic)
        )
    }
}

/**
 * Defines all the fonts available to use in the library.
 * Each definition includes a number of preset TextStyle that
 * can be used within Compose.
 *
 * Separately, custom font styles can be defined using the `font` function.
 */
enum class FontDef {
    System;

    companion object {
        /**
         * Construct from a String Value using the enum name of the font.
         * If there is no mach, .System font is returned.
         */
        fun from(value: String?): FontDef {
            return values().find { it.name == value } ?: System
        }
    }

    /**
     * The FontFamily is used internally to construct text styles.
     */
    private val fontFamily: FontFamily
        get() = when (this) {
            System -> FontFamily.Default
        }


    /**
     * Human Readable Font Name.
     */
    val fontName: String
        get() = when (this) {
            System -> "System Font"
        }

    /**
     * Construct custom a TextStyle using common defaults
     * and the current FontFamily defined.
     */
    private fun createStyle(
        size: TextUnit,
        weight: FontWeight = FontWeight.Normal,
        style: FontStyle = FontStyle.Normal,
        letterSpacing: TextUnit = TextUnit.Unspecified
    ): TextStyle {
        return TextStyle(
            fontFamily = this.fontFamily,
            fontSize = size,
            fontWeight = weight,
            fontStyle = style,
            letterSpacing = letterSpacing,
        )
    }

    /**
     * ============== Predefined Text Styles ==============
     */
    val typography: Typography
        get() = Typography(
            displayLarge = createStyle(size = 24.sp),
            displayMedium = createStyle(size = 22.sp),
            displaySmall = createStyle(size = 20.sp),

            headlineLarge = createStyle(size = 28.sp),
            headlineMedium = createStyle(size = 26.sp),
            headlineSmall = createStyle(size = 24.sp),

            titleLarge = createStyle(size = 30.sp, weight = FontWeight.Bold, letterSpacing = 0.5.sp),
            titleMedium = createStyle(size = 28.sp, weight = FontWeight.Bold, letterSpacing = 0.2.sp),
            titleSmall = createStyle(size = 24.sp, weight = FontWeight.SemiBold),

            bodyLarge = createStyle(size = 18.sp),
            bodyMedium = createStyle(size = 16.sp),
            bodySmall = createStyle(size = 14.sp),

            labelSmall = createStyle(size = 12.sp),
            labelMedium = createStyle(size = 16.sp, weight = FontWeight.SemiBold),
            labelLarge = createStyle(size = 18.sp, weight = FontWeight.Bold),
        )
}
