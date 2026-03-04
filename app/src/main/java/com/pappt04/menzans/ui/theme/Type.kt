package com.pappt04.menzans.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import com.pappt04.menzans.R
import androidx.compose.ui.unit.sp

val provider =
    GoogleFont.Provider(
        providerAuthority = "com.google.android.gms.fonts",
        providerPackage = "com.google.android.gms",
        certificates = R.array.com_google_android_gms_fonts_certs,
    )

val bodyFontFamily =
    FontFamily(
        Font(
            googleFont = GoogleFont("Outfit"),
            fontProvider = provider,
        ),
    )

val displayFontFamily =
    FontFamily(
        Font(
            googleFont = GoogleFont("Outfit"),
            fontProvider = provider,
        ),
    )

val megatitleFont =
    FontFamily(
        Font(
            googleFont = GoogleFont("Outfit"),
            fontProvider = provider,
            weight = androidx.compose.ui.text.font.FontWeight.Bold
        ),
    )

// Default Material 3 typography values
val baseline = Typography()

val AppTypography =
    Typography(
        displayLarge = baseline.displayLarge.copy(fontFamily = displayFontFamily, letterSpacing = (-1.0).sp),
        displayMedium = baseline.displayMedium.copy(fontFamily = displayFontFamily, letterSpacing = (-0.5).sp),
        displaySmall = baseline.displaySmall.copy(fontFamily = displayFontFamily, letterSpacing = (-0.5).sp),
        headlineLarge = baseline.headlineLarge.copy(fontFamily = displayFontFamily, letterSpacing = (-0.5).sp),
        headlineMedium = baseline.headlineMedium.copy(fontFamily = displayFontFamily, letterSpacing = (-0.5).sp),
        headlineSmall = baseline.headlineSmall.copy(fontFamily = displayFontFamily),
        titleLarge = baseline.titleLarge.copy(fontFamily = displayFontFamily, fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold),
        titleMedium = baseline.titleMedium.copy(fontFamily = displayFontFamily, fontWeight = androidx.compose.ui.text.font.FontWeight.Medium),
        titleSmall = baseline.titleSmall.copy(fontFamily = displayFontFamily, fontWeight = androidx.compose.ui.text.font.FontWeight.Medium),
        bodyLarge = baseline.bodyLarge.copy(fontFamily = bodyFontFamily, lineHeight = 24.sp),
        bodyMedium = baseline.bodyMedium.copy(fontFamily = bodyFontFamily, lineHeight = 20.sp),
        bodySmall = baseline.bodySmall.copy(fontFamily = bodyFontFamily, lineHeight = 16.sp),
        labelLarge = baseline.labelLarge.copy(fontFamily = bodyFontFamily),
        labelMedium = baseline.labelMedium.copy(fontFamily = bodyFontFamily),
        labelSmall = baseline.labelSmall.copy(fontFamily = bodyFontFamily),
    )
