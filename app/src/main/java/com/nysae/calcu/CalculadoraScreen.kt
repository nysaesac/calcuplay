package com.nysae.calcu

import android.content.SharedPreferences
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.ui.text.style.TextAlign

fun getCurrencySymbol(moneda: String = "PEN"): String {
    return when (moneda.uppercase(Locale.getDefault())) {
        "PEN" -> "S/."
        "USD" -> "$"
        "EUR" -> "€"
        "MXN" -> "MX$"
        "COP" -> "COL$"
        else -> "$"
    }
}

@Composable
fun CalculadoraScreen(
    prefs: SharedPreferences,
    isPro: State<Boolean>,
    onVerHistorial: () -> Unit,
    onVolver: () -> Unit,
    onIrPro: () -> Unit
) {
    val context = LocalContext.current
    val activity = context as MainActivity
    val idioma = LocalIdiomaViewModel.current
    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // ===================== ESTADOS =====================
    var cliente by rememberSaveable { mutableStateOf(prefs.getString("NombreCliente", "") ?: "Cliente") }

    val monedaCliente = prefs.getString("MONEDA_${cliente}", "PEN") ?: "PEN"
    val currencySymbol = getCurrencySymbol(monedaCliente)

    // 🔹 Variables renombradas para artefactos eléctricos
    var nombreEquipo by rememberSaveable { mutableStateOf(prefs.getString("NombreEquipo_${cliente}", "") ?: "") }
    var amp by rememberSaveable { mutableStateOf(prefs.getString("AMP_${cliente}", "") ?: "") }
    var ma by rememberSaveable { mutableStateOf(prefs.getString("MA_${cliente}", "") ?: "") }
    var voltaje by rememberSaveable { mutableStateOf(prefs.getString("VOLTAJE_${cliente}", "") ?: "") }
    var horas by rememberSaveable { mutableStateOf(prefs.getString("HORAS_${cliente}", "") ?: "") }
    var minutos by rememberSaveable { mutableStateOf(prefs.getString("MINUTOS_${cliente}", "") ?: "") }
    var costoKwh by rememberSaveable { mutableStateOf(prefs.getString("COSTO_KWH_${cliente}", "") ?: "") }

    var mostrarResultados by rememberSaveable { mutableStateOf(prefs.getBoolean("MOSTRAR_RESULTADOS", false)) }
    var valoresModificados by remember { mutableStateOf(false) }

    // ⭐ CRÉDITOS (AQUÍ VA)
    var creditos by remember { mutableStateOf(0) }
    var ultimoCalculo by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(Unit) {
        creditos = CreditosManager.obtenerCreditos(prefs)
    }

    // ===================== CARGAR VARIABLES CUANDO CAMBIA CLIENTE =====================
    LaunchedEffect(cliente) {
        nombreEquipo = prefs.getString("NombreEquipo_${cliente}", "") ?: ""
        amp = prefs.getString("AMP_${cliente}", "") ?: ""
        ma = prefs.getString("MA_${cliente}", "") ?: ""
        voltaje = prefs.getString("VOLTAJE_${cliente}", "") ?: ""
        horas = prefs.getString("HORAS_${cliente}", "") ?: ""
        minutos = prefs.getString("MINUTOS_${cliente}", "") ?: ""
        costoKwh = prefs.getString("COSTO_KWH_${cliente}", "") ?: ""
    }

    // ===================== GUARDAR AUTOMÁTICAMENTE =====================
    LaunchedEffect(cliente, amp, ma, voltaje, horas, minutos, costoKwh, nombreEquipo, mostrarResultados) {
        with(prefs.edit()) {
            putString("CLIENTE", cliente)
            putString("NombreEquipo_${cliente}", nombreEquipo)
            putString("AMP_${cliente}", amp)
            putString("MA_${cliente}", ma)
            putString("VOLTAJE_${cliente}", voltaje)
            putString("HORAS_${cliente}", horas)
            putString("MINUTOS_${cliente}", minutos)
            putString("COSTO_KWH_${cliente}", costoKwh)
            putBoolean("MOSTRAR_RESULTADOS", mostrarResultados)
            apply()
        }
    }

    fun onValorModificado() {
        if (mostrarResultados) {
            mostrarResultados = false
            valoresModificados = true
            scope.launch {
                snackbarHostState.showSnackbar(idioma.texto("snackbarValoresModificados"))
            }
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ===================== ENCABEZADO DE CLIENTE =====================
            var nombreCliente by remember { mutableStateOf(prefs.getString("NombreCliente", "") ?: "") }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = idioma.texto("clienteTitulo"),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Button(
                        onClick = { onVolver() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                        modifier = Modifier.height(26.dp)
                    ) {
                        Text(
                            text = idioma.texto("cambiarCliente"),
                            fontSize = 11.sp,
                            color = Color.White
                        )
                    }
                }

                Text(
                    text = nombreCliente.ifBlank { idioma.texto("clienteSinAsignar") },
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0D47A1),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            // ===================== LOGO NYSAE =====================
            Cabecera()
            BotonIrPro(isPro = isPro.value, onIrPro = onIrPro)

            // ===================== Nombre del Equipo =====================
            OutlinedTextField(
                value = nombreEquipo,
                onValueChange = {
                    nombreEquipo = it
                    prefs.edit().putString("NombreEquipo_${cliente}", nombreEquipo).apply()
                    onValorModificado()
                },
                label = { Text(idioma.texto("nombreEquipo")) },
                modifier = Modifier
                    .fillMaxWidth(),
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(fontSize = 16.sp, textAlign = TextAlign.Center)
            )

            // ===================== AMP y mA =====================
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = amp,
                    onValueChange = {
                        amp = it.filter { c -> c.isDigit() || c == '.' }
                        onValorModificado()
                    },
                    label = { Text(idioma.texto("amp"), fontSize = 13.sp) },
                    modifier = Modifier.weight(1f).height(70.dp),
                    singleLine = true
                )

                OutlinedTextField(
                    value = ma,
                    onValueChange = {
                        ma = it.filter { c -> c.isDigit() || c == '.' }
                        onValorModificado()
                    },
                    label = { Text(idioma.texto("ma"), fontSize = 13.sp) },
                    modifier = Modifier.weight(1f).height(70.dp),
                    singleLine = true
                )
            }

            // ===================== Horas y Minutos =====================
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = horas,
                    onValueChange = {
                        val nuevo = it.filter { c -> c.isDigit() }
                        horas = when {
                            nuevo.isBlank() -> ""
                            (nuevo.toIntOrNull() ?: 0) > 23 -> "23"
                            else -> nuevo
                        }
                        onValorModificado()
                    },
                    label = { Text(idioma.texto("horas"), fontSize = 13.sp) },
                    modifier = Modifier.weight(1f).height(70.dp),
                    singleLine = true
                )

                OutlinedTextField(
                    value = minutos,
                    onValueChange = {
                        val nuevo = it.filter { c -> c.isDigit() }
                        minutos = when {
                            nuevo.isBlank() -> ""
                            (nuevo.toIntOrNull() ?: 0) > 59 -> "59"
                            else -> nuevo
                        }
                        onValorModificado()
                    },
                    label = { Text(idioma.texto("minutos"), fontSize = 13.sp) },
                    modifier = Modifier.weight(1f).height(70.dp),
                    singleLine = true
                )
            }

            // ===================== Voltaje y Costo kWh =====================
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = voltaje,
                    onValueChange = {
                        voltaje = it.filter { c -> c.isDigit() || c == '.' }
                        onValorModificado()
                    },
                    label = { Text(idioma.texto("voltaje"), fontSize = 13.sp) },
                    modifier = Modifier.weight(1f).height(70.dp),
                    singleLine = true
                )

                OutlinedTextField(
                    value = costoKwh,
                    onValueChange = {
                        costoKwh = it.filter { c -> c.isDigit() || c == '.' }
                        onValorModificado()
                    },
                    label = { Text("${currencySymbol} kWh", fontSize = 13.sp) },
                    modifier = Modifier.weight(1f).height(70.dp),
                    singleLine = true
                )
            }
            Spacer(modifier = Modifier.height(10.dp))   // ⭐ espacio perfecto

            // ===================== CRÉDITOS =====================
            if (!isPro.value) {
                Text(
                    text = "${idioma.texto("creditosDisponibles")} $creditos",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (creditos > 0) Color(0xFF2E7D32) else Color.Red,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            // ===================== Botón Calcular =====================
            Button(
                onClick = {
                    val calculoActual = "$amp|$ma|$voltaje|$horas|$minutos|$costoKwh"

// 1. evitar repetido primero
                    if (!isPro.value && calculoActual == ultimoCalculo) {
                        scope.launch {
                            snackbarHostState.showSnackbar(idioma.texto("calculoRepetido"))
                        }
                        return@Button
                    }

                   // 2. validar créditos
                    if (!isPro.value && creditos <= 0) {
                        scope.launch {
                            snackbarHostState.showSnackbar(idioma.texto("sinCreditos"))
                        }
                        return@Button
                    }

                    // 3. recién guardar último cálculo
                    ultimoCalculo = calculoActual
                    if ((amp.isBlank() && ma.isBlank()) || voltaje.isBlank() || costoKwh.isBlank() || (horas.isBlank() && minutos.isBlank())) {
                        scope.launch {
                            snackbarHostState.showSnackbar(idioma.texto("completarCampos"))
                        }
                    } else {
                        val amperios = amp.toDoubleOrNull() ?: 0.0
                        val miliamperios = ma.toDoubleOrNull() ?: 0.0
                        val corrienteTotal = amperios + (miliamperios / 1000.0)
                        val v = voltaje.toDoubleOrNull() ?: 0.0
                        val precio = costoKwh.toDoubleOrNull() ?: 0.0
                        val h = horas.toIntOrNull() ?: 0
                        val m = minutos.toIntOrNull() ?: 0
                        val totalHoras = h + m / 60.0

                        val potenciaW = v * corrienteTotal
                        val potenciaKW = potenciaW / 1000.0
                        val consumo = potenciaKW * totalHoras
                        val consumoMensual = consumo * 30
                        val costoDia = consumo * precio
                        val costoMensual = costoDia * 30

                        val fechaActual = SimpleDateFormat("dd/MM/yy HH:mm", Locale("es", "ES")).format(Date())

                        val resultadoTexto = """
📊 $nombreEquipo
📅 $fechaActual

${idioma.texto("potenciaW")}: ${"%.2f".format(potenciaW)} W 
${idioma.texto("potenciaKW")}: ${"%.2f".format(potenciaKW)} kW 
${idioma.texto("kwhDia")}: ${"%.2f".format(consumo)} kWh
${idioma.texto("kwhMes")}: ${"%.2f".format(consumoMensual)} kWh
${idioma.texto("costoDia")}: $currencySymbol ${"%.2f".format(costoDia)}
${idioma.texto("costoMes")}: $currencySymbol ${"%.2f".format(costoMensual)}
========================
""".trimIndent()

                        // Guardar historial
                        val clienteHistorial = prefs.getString("NombreCliente", "") ?: ""
                        if (clienteHistorial.isNotBlank()) {
                            val claveHistorial = "HistorialCalculadora_${clienteHistorial}"
                            val historialExistente = prefs.getString(claveHistorial, "") ?: ""
                            if (!historialExistente.contains(resultadoTexto)) {
                                val nuevoHistorial = if (historialExistente.isNotBlank()) "$resultadoTexto|$historialExistente" else resultadoTexto
                                prefs.edit().putString(claveHistorial, nuevoHistorial).apply()
                            }
                        }
                   // ⭐ RESTAR CRÉDITO (IMPORTANTE)
                        CreditosManager.usarCredito(prefs)

                        creditos = CreditosManager.obtenerCreditos(prefs)

                        mostrarResultados = true
                    }
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC0C0C0))
            ) {
                Text(
                    text = idioma.texto("calcularBtn"),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
            // boton de creditos
            if (!isPro.value) {
                Button(
                    onClick = {
                        CreditosAdsManager.mostrar(activity, prefs) {
                            creditos = CreditosManager.obtenerCreditos(prefs)
                        }
                    }
                ) {
                    Text(idioma.texto("verVideoCreditos"))
                }
            }
// ===================== Resultados =====================
            if (mostrarResultados) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(6.dp)
                ) {
                    Column(Modifier.padding(12.dp)) {

                        val fechaHora = SimpleDateFormat("dd/MM/yy HH:mm", Locale.getDefault()).format(Date())

                        // --- Título ---
                        Text("${idioma.texto("resumenTitulo")} $nombreEquipo",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text("${idioma.texto("resumenFecha")}: $fechaHora",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        Spacer(Modifier.height(6.dp))

                        // --- Cálculos originales (NO TOCADO) ---
                        val amperios = amp.toDoubleOrNull() ?: 0.0
                        val miliamperios = ma.toDoubleOrNull() ?: 0.0
                        val corrienteTotal = amperios + (miliamperios / 1000.0)
                        val v = voltaje.toDoubleOrNull() ?: 0.0
                        val h = horas.toIntOrNull() ?: 0
                        val m = minutos.toIntOrNull() ?: 0
                        val totalHoras = h + m / 60.0
                        val precio = costoKwh.toDoubleOrNull() ?: 0.0

                        val potenciaW = v * corrienteTotal
                        val potenciaKW = potenciaW / 1000.0
                        val consumo = potenciaKW * totalHoras
                        val consumoMensual = consumo * 30
                        val costoDia = consumo * precio
                        val costoMensual = costoDia * 30

                        // --- Items con iconos ---
                        val items = listOf(
                            Triple("potenciaW", "${"%.2f".format(potenciaW)} W", Icons.Default.Bolt),
                            Triple("potenciaKW", "${"%.2f".format(potenciaKW)} kW", Icons.Default.FlashOn),
                            Triple("kwhDia", "${"%.2f".format(consumo)} kWh", Icons.Default.EnergySavingsLeaf),
                            Triple("kwhMes", "${"%.2f".format(consumoMensual)} kWh", Icons.Default.Timeline),
                            Triple("costoDia", "$currencySymbol ${"%.2f".format(costoDia)}", Icons.Default.Money),
                            Triple("costoMes", "$currencySymbol ${"%.2f".format(costoMensual)}", Icons.Default.CalendarMonth)
                        )

                        // --- Colores por campo ---
                        fun colorFor(key: String) = when (key) {
                            "potenciaW" -> Color(0xFF2196F3)
                            "potenciaKW" -> Color(0xFF1976D2)
                            "kwhDia" -> Color(0xFF4CAF50)
                            "kwhMes" -> Color(0xFF2E7D32)
                            "costoDia" -> Color(0xFFFFA000)
                            "costoMes" -> Color(0xFFB71C1C)
                            else -> Color.Black
                        }

                        // --- Fila con icono + label + valor ---
                        items.forEach { (key, valor, icon) ->
                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        icon,
                                        contentDescription = null,
                                        tint = colorFor(key),
                                        modifier = Modifier.size(22.dp)
                                    )
                                    Spacer(Modifier.width(6.dp))
                                    Text(
                                        idioma.texto(key),
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                                Text(
                                    valor,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.height(12.dp))
                BannerAd(isPro = isPro.value)
                Spacer(Modifier.height(12.dp))
            }


            // ===================== Botón historial =====================
            Button(
                onClick = { onVerHistorial() },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
            ) {
                Icon(Icons.Default.History, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text(idioma.texto("verHistorialRecibos"))
            }
        } // ← Column
    } // ← Scaffold
}
