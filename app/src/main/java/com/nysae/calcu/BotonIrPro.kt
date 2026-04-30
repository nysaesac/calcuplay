package com.nysae.calcu

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color     // ← FALTA ESTO
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp          // ← FALTA ESTO

@Composable
fun BotonIrPro(isPro: Boolean, onIrPro: () -> Unit) {
    val idioma = LocalIdiomaViewModel.current
    if (!isPro) {
        Button(
            onClick = onIrPro,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            Text(idioma.texto("mejorarPro"))
        }
        Spacer(modifier = Modifier.height(12.dp))
    } else {
        // ⭐ Mensaje de agradecimiento para usuarios PRO
        Text(
            idioma.texto("graciasPro"),
            color = Color(0xFF2E7D32),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(12.dp))
    }
}
