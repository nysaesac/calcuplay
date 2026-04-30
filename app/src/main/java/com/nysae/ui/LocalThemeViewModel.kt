package com.nysae.ui

import androidx.compose.runtime.staticCompositionLocalOf

val LocalThemeViewModel = staticCompositionLocalOf<ThemeViewModel> {
    error("No ThemeViewModel provided")
}
