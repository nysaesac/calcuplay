package com.nysae.calcu

import androidx.compose.runtime.staticCompositionLocalOf
import com.nysae.calcu.IdiomaViewModel

val LocalIdiomaViewModel = staticCompositionLocalOf<IdiomaViewModel> {
    error("IdiomaViewModel no encontrado")
}
