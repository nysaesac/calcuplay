package com.nysae.calcu

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun HistorialScreen(
    prefs: SharedPreferences,
    isPro: State<Boolean>,
    onVolver: () -> Unit,
    onIrPro: () -> Unit
) {
    val idioma = LocalIdiomaViewModel.current
    val mainActivity = LocalMainActivity.current
    val context = LocalContext.current
    val historial = remember { mutableStateListOf<String>() }
    val seleccionados = remember { mutableStateListOf<String>() }

    var showDialogBorrarTodo by remember { mutableStateOf(false) }
    var showDialogBorrarUno by remember { mutableStateOf<String?>(null) }

    val nombreCliente = prefs.getString("NombreCliente", "") ?: ""
    val claveHistorial: String? =
        if (nombreCliente.isNotBlank()) "HistorialCalculadora_${nombreCliente}" else null

    // 🚫 BLOQUEO COMPLETO SI NO HAY CLIENTE
    if (nombreCliente.isBlank()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = idioma.texto("asignarClientePrimero"),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Red,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(onClick = { onVolver() }) {
                Text(idioma.texto("volverBtn"))
            }
        }
        return
    }
    // 🚫 FIN BLOQUEO


    LaunchedEffect(nombreCliente) {
        historial.clear()
        val guardado = if (claveHistorial != null)
            prefs.getString(claveHistorial, "") ?: ""
        else
            ""
        if (guardado.isNotEmpty()) {
            historial.addAll(guardado.split("|").filter { it.isNotBlank() })
        }
    }

    // Mostrar historial temporal para no PRO
    var showProMessage by remember { mutableStateOf(false) }

    if (!isPro.value) {
        LaunchedEffect(historial) {
            delay(5000)
            showProMessage = true
            delay(5000)
            onVolver()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .navigationBarsPadding(), // 👈 SOLUCIÓN
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Cabecera()

        if (!isPro.value) {
            BotonIrPro(
                isPro = isPro.value,
                onIrPro = { onIrPro() }
            )
            Spacer(modifier = Modifier.height(8.dp))   // espacio normal si NO es PRO
        } else {
            Spacer(modifier = Modifier.height(2.dp))   // casi sin espacio si es PRO
        }

        Text(
            text = idioma.texto("historialTituloConNombre").format(nombreCliente),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        BannerAd(isPro = isPro.value)

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = idioma.texto("historialAcciones"),
                fontSize = 14.sp,
                color = Color(0xFF888888),
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = { showDialogBorrarTodo = true }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = idioma.texto("borrarTodoTitulo"),
                    tint = Color.Red
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(historial) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Column(modifier = Modifier.weight(1f)) {
                            val lineas = item.split("\n")
                            Text(
                                text = lineas.firstOrNull() ?: "",
                                fontSize = 14.2.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF1565C0),
                                modifier = Modifier.padding(bottom = 0.dp)
                            )
                            if (lineas.size > 1) {
                                Text(
                                    text = lineas.drop(1).filter { it.isNotBlank() }.joinToString("\n"),
                                    fontSize = 14.sp
                                )
                            }
                        }

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(start = 8.dp)
                        ) {
                            Checkbox(
                                checked = seleccionados.contains(item),
                                onCheckedChange = { checked ->
                                    if (checked) seleccionados.add(item)
                                    else seleccionados.remove(item)
                                }
                            )
                            IconButton(onClick = { showDialogBorrarUno = item }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = idioma.texto("borrarEste"),
                                    tint = Color.Red
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (!isPro.value && showProMessage) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = idioma.texto("soloUsuariosPro"),
                    color = Color.Red,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }

        Text(
            text = idioma.texto("seleccionRegistros"),
            fontSize = 12.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { onVolver() }) {
                Text(idioma.texto("volverBtn"))
            }

            Button(
                onClick = {

                    if (!isPro.value) {
                        Toast.makeText(context, idioma.texto("soloPro"), Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    if (historial.isEmpty()) {
                        Toast.makeText(context, idioma.texto("historialVacio"), Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    val mensaje = if (seleccionados.isEmpty())
                        historial.joinToString("\n")
                    else
                        seleccionados.joinToString("\n")

                    var codigoPais = prefs.getString("CodigoPaisCliente", "") ?: ""
                    var celularCliente = prefs.getString("CelularCliente", "") ?: ""

                    if (codigoPais.isBlank() || celularCliente.isBlank()) {
                        Toast.makeText(context, idioma.texto("faltaCodigoNumero"), Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    codigoPais = codigoPais.replace(" ", "").replace("+", "")
                    celularCliente = celularCliente.replace(" ", "").replace("+", "")

                    val numeroFinal = codigoPais + celularCliente
                    val url = "https://wa.me/$numeroFinal?text=${Uri.encode(mensaje)}"
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    mainActivity.startActivity(intent)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366))
            ) {
                Text(idioma.texto("enviarWhatsappBtn"), color = Color.White)
            }
        }

        // --- AlertDialogs de borrado ---
        if (showDialogBorrarTodo) {
            AlertDialog(
                onDismissRequest = { showDialogBorrarTodo = false },
                title = { Text(idioma.texto("borrarTodoTitulo")) },
                text = { Text(idioma.texto("borrarTodoTexto")) },
                confirmButton = {
                    TextButton(onClick = {
                        historial.clear()
                        prefs.edit().apply {
                            if (claveHistorial != null) {
                                remove(claveHistorial)
                            }
                            apply()
                        }
                        showDialogBorrarTodo = false
                        Toast.makeText(context, idioma.texto("historialEliminado"), Toast.LENGTH_SHORT).show()
                    }) {
                        Text(idioma.texto("siBorrarTodo"), color = Color.Red)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialogBorrarTodo = false }) {
                        Text(idioma.texto("cancelar"))
                    }
                }
            )
        }

        if (showDialogBorrarUno != null) {
            AlertDialog(
                onDismissRequest = { showDialogBorrarUno = null },
                title = { Text(idioma.texto("borrarUnoTitulo")) },
                text = { Text(idioma.texto("borrarUnoTexto")) },
                confirmButton = {
                    TextButton(onClick = {
                        historial.remove(showDialogBorrarUno)
                        if (claveHistorial != null) {
                            prefs.edit().putString(claveHistorial, historial.joinToString("|")).apply()
                        }
                        showDialogBorrarUno = null
                    }) {
                        Text(idioma.texto("eliminarRegistro"), color = Color.Red)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialogBorrarUno = null }) {
                        Text(idioma.texto("cancelar"))
                    }
                }
            )
        }
    }
}
