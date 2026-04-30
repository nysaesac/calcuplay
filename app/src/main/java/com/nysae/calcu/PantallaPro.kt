package com.nysae.calcu

import android.content.SharedPreferences
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.isSystemInDarkTheme

@Composable
fun PantallaPro(
    prefs: SharedPreferences,
    isProState: State<Boolean>,
    onProActivado: () -> Unit,
    onVolver: () -> Unit
) {
    val context = LocalContext.current
    val activity = LocalMainActivity.current
    val idioma = LocalIdiomaViewModel.current

    var isPro by remember { mutableStateOf(isProState.value) }

    val billingManager = remember {
        BillingClientManager(context) { nuevoEstado: Boolean ->
            isPro = nuevoEstado
            if (nuevoEstado) {
                onProActivado()
            }
        }
    }

    LaunchedEffect(Unit) {
        billingManager.startConnection()
        billingManager.checkActiveSubscriptions()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Cabecera()

        Spacer(modifier = Modifier.height(16.dp))

        Text(idioma.texto("mejorarPro"), fontSize = 22.sp)

        Spacer(modifier = Modifier.height(12.dp))

        val isDark = isSystemInDarkTheme()
        val textoColor = if (isDark) Color(0xFFEEEEEE) else Color(0xFF222222)

        Text("✔ " + idioma.texto("proSinAnuncios"), fontSize = 16.sp, color = textoColor)
        Spacer(modifier = Modifier.height(4.dp))

        Text("✔ " + idioma.texto("proHistorialIlimitado"), fontSize = 16.sp, color = textoColor)
        Spacer(modifier = Modifier.height(4.dp))

        Text("✔ " + idioma.texto("proFuncionesExclusivas"), fontSize = 16.sp, color = textoColor)

        Spacer(modifier = Modifier.height(30.dp))

        // ✅ BOTÓN MENSUAL (VISUAL IGUAL)
        Button(
            onClick = {
                billingManager.comprarSuscripcion(
                    activity = activity,
                    productId = "pro",
                    basePlanId = "monthly"
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(idioma.texto("proSuscripcionMensual"))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ✅ BOTÓN ANUAL (VISUAL IGUAL)
        Button(
            onClick = {
                billingManager.comprarSuscripcion(
                    activity = activity,
                    productId = "pro",
                    basePlanId = "yearly"
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(idioma.texto("proSuscripcionAnual"))
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (isPro) {
            Text(idioma.texto("proYaEresPro"), color = Color(0xFF2E7D32))
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = { onVolver() }) {
            Text(idioma.texto("proVolver"))
        }
    }
}
