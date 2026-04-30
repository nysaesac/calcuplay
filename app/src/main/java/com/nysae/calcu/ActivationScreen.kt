package com.nysae.calcu

import android.content.SharedPreferences
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.clickable

@Composable
fun ActivationScreen(
    prefs: SharedPreferences,
    onActivated: () -> Unit,
    onIrPro: () -> Unit
) {
    val idioma = LocalIdiomaViewModel.current
    var seudonimo by remember { mutableStateOf("") }

    // Para el diálogo de cambio de usuario
    var mostrarDialogoCambio by remember { mutableStateOf(false) }

    // Control error seudónimo obligatorio
    var errorSeudonimo by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Cabecera()

        // 🔄 BOTÓN CAMBIO DE IDIOMA
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp)
                .clickable { idioma.cambiarIdioma() }
        ) {

            Text(
                text = if (idioma.idiomaEs)
                    idioma.texto("idiomaEspanol")
                else
                    idioma.texto("idiomaIngles"),
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(1f)
            )

            Switch(
                checked = !idioma.idiomaEs,
                onCheckedChange = { idioma.cambiarIdioma() }
            )
        }

        // ✔ Botón PRO funcionando correctamente
        BotonIrPro(
            isPro = false,
            onIrPro = { onIrPro() }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            idioma.texto("bienvenidaActivacion"),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = seudonimo,
            onValueChange = {
                seudonimo = it
                errorSeudonimo = false
            },
            label = { Text(idioma.texto("ingreseSeudonimo")) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = errorSeudonimo
        )

        if (errorSeudonimo) {
            Text(
                text = idioma.texto("errorSeudonimoObligatorio"),
                color = Color.Red,
                fontSize = 14.sp,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (seudonimo.trim().isEmpty()) {
                    errorSeudonimo = true
                    return@Button
                }

                prefs.edit()
                    .putString("UserNickname", seudonimo.trim())
                    .apply()

                onActivated()
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                idioma.texto("btnIngresar"),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.White
            )
        }

        // ⭐ DIÁLOGO DE CAMBIO DE USUARIO
        if (mostrarDialogoCambio) {
            var nuevoSeudonimo by remember { mutableStateOf("") }

            AlertDialog(
                onDismissRequest = { mostrarDialogoCambio = false },
                title = { Text("Cambiar usuario") },
                text = {
                    OutlinedTextField(
                        value = nuevoSeudonimo,
                        onValueChange = { nuevoSeudonimo = it },
                        label = { Text("Nuevo seudónimo") }
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        prefs.edit()
                            .putString("UserNickname", nuevoSeudonimo.trim())
                            .apply()
                        mostrarDialogoCambio = false
                    }) {
                        Text("Guardar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { mostrarDialogoCambio = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    } // ← cierre correcto de Column
}
