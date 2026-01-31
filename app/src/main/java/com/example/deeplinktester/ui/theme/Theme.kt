package com.example.deeplinktester.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.example.deeplinktester.R

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

private val FiraSans = FontFamily(
//    Font(R.font.fira_sans_light, weight = FontWeight.Light),
    Font(R.font.fira_sans, weight = FontWeight.Normal),
    Font(R.font.fira_sans_medium, weight = FontWeight.Medium),
//    Font(R.font.fira_sans_semibold, weight = FontWeight.SemiBold),
//    Font(R.font.fira_sans_bold, weight = FontWeight.Bold)
)

private val FiraSansTypography = Typography().run {
    copy(
        displayLarge = displayLarge.copy(fontFamily = FiraSans),
        displayMedium = displayMedium.copy(fontFamily = FiraSans),
        displaySmall = displaySmall.copy(fontFamily = FiraSans),
        headlineLarge = headlineLarge.copy(fontFamily = FiraSans),
        headlineMedium = headlineMedium.copy(fontFamily = FiraSans),
        headlineSmall = headlineSmall.copy(fontFamily = FiraSans),
        titleLarge = titleLarge.copy(fontFamily = FiraSans),
        titleMedium = titleMedium.copy(fontFamily = FiraSans),
        titleSmall = titleSmall.copy(fontFamily = FiraSans),
        bodyLarge = bodyLarge.copy(fontFamily =  FiraSans),
        bodyMedium = bodyMedium.copy(fontFamily = FiraSans),
        bodySmall = bodySmall.copy(fontFamily = FiraSans),
        labelLarge = labelLarge.copy(fontFamily = FiraSans),
        labelMedium = labelMedium.copy(fontFamily = FiraSans),
        labelSmall = labelSmall.copy(fontFamily = FiraSans)
    )
}

@Composable
fun DeeplinkTesterTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content,
        typography = FiraSansTypography
    )
}