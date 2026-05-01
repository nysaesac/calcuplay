package com.nysae.calcu

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.nysae.ui.LocalThemeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClienteScreen(
    prefs: SharedPreferences,
    isPro: State<Boolean>,
    onContinuar: () -> Unit,
    onIrPro: () -> Unit
) {
    val idioma = LocalIdiomaViewModel.current
    var menuAbierto by remember { mutableStateOf(false) }
    var mostrarDialogoCambio by remember { mutableStateOf(false) }

    var nombreCliente by remember {
        mutableStateOf(prefs.getString("NombreCliente", "") ?: "")
    }

    var celularCliente by remember {
        mutableStateOf(prefs.getString("CelularCliente", "") ?: "")
    }

    // ⭐ NUEVO (necesario para que funcione tu código)
    var codigoPais by remember {
        mutableStateOf(prefs.getString("CodigoPaisCliente", "") ?: "")
    }

    var clientesGuardados by remember {
        mutableStateOf(
            prefs.getString("ListaClientes", "")
                ?.split("|")
                ?.filter { it.isNotBlank() }
                ?.map { it to "" }
                ?: emptyList()
        )
    }

    var expanded by remember { mutableStateOf(false) }
    var selectedCliente by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {

        //---------------------------------------------------------------------
        //        TU CONTENIDO PRINCIPAL (NO LO TOQUÉ)
        //---------------------------------------------------------------------
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(12.dp))

            Cabecera()

            BotonIrPro(
                isPro = isPro.value,
                onIrPro = { onIrPro() }
            )

            Spacer(modifier = Modifier.height(8.dp))

            val seudonimoGuardado = prefs.getString("UserNickname", "") ?: ""

// 🔥 ViewModel del tema
            val themeViewModel = LocalThemeViewModel.current

// 🔥 ViewModel del idioma (ÚNICAMENTE AQUÍ, no más arriba)
            val idioma = LocalIdiomaViewModel.current

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu",
                    tint = if (themeViewModel.isDarkTheme.value) Color.White else Color.Black,
                    modifier = Modifier
                        .size(28.dp)
                        .clickable { menuAbierto = true }
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "${idioma.texto("bienvenida")} $seudonimoGuardado, ${idioma.texto("ingresaDatosCliente")}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }//  ← ← ← CERRAMOS AQUÍ EL ROW

//--------------------------------------------------------------

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = nombreCliente,
                onValueChange = { nombreCliente = it },
                label = { Text(idioma.texto("nombreCliente")) },   // ← CAMBIO
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Código de país
                OutlinedTextField(
                    value = codigoPais,
                    onValueChange = { codigoPais = it },
                    label = { Text(idioma.texto("codigo")) },       // ← CAMBIO
                    modifier = Modifier.weight(1f)
                )

                // Celular del cliente
                OutlinedTextField(
                    value = celularCliente,
                    onValueChange = { celularCliente = it },
                    label = { Text(idioma.texto("celular")) },      // ← CAMBIO
                    modifier = Modifier.weight(2f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))


//------------------------------------
//      SELECCIÓN DE MONEDA
//------------------------------------

            var moneda by remember {
                mutableStateOf(prefs.getString("MonedaCliente", "PEN") ?: "PEN")
            }

            val listaMonedas = listOf(
                "PEN – Sol peruano",
                "USD – Dólar americano",
                "EUR – Euro",
                "MXN – Peso mexicano",
                "CLP – Peso chileno",
                "COP – Peso colombiano",
                "ARS – Peso argentino"
            )

            var expandedMoneda by remember { mutableStateOf(false) }

            Box {
                OutlinedTextField(
                    value = moneda,
                    onValueChange = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expandedMoneda = true },
                    readOnly = true,
                    enabled = false,
                    label = { Text(idioma.texto("monedaCalculos")) },
                    textStyle = LocalTextStyle.current.copy(
                        color = MaterialTheme.colorScheme.onBackground   // ✔ se ve siempre
                    )
                )

                DropdownMenu(
                    expanded = expandedMoneda,
                    onDismissRequest = { expandedMoneda = false }
                ) {
                    listaMonedas.forEach { item ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = item,
                                    color = MaterialTheme.colorScheme.onSurface   // ✔ seguro para oscuro
                                )
                            },
                            onClick = {
                                moneda = item.substringBefore(" ")
                                prefs.edit()
                                    .putString("MONEDA_${nombreCliente}", moneda)
                                    .apply()
                                expandedMoneda = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            //--------------------------
            //    LISTA DE CLIENTES
            //--------------------------
            if (clientesGuardados.isNotEmpty()) {

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = selectedCliente,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(idioma.texto("clientesRegistrados")) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        clientesGuardados.forEach { (nombre, celular) ->

                            var showDialog by remember { mutableStateOf(false) }

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            ) {
                                Text(text = nombre, fontWeight = FontWeight.Bold)

                                Spacer(modifier = Modifier.height(4.dp))

                                Row {

                                    if (isPro.value) {
                                        TextButton(onClick = {
                                            selectedCliente = nombre
                                            nombreCliente = nombre
                                            celularCliente = celular
                                            prefs.edit()
                                                .putString("NombreCliente", nombre)
                                                .putString("CelularCliente", celular)
                                                .apply()
                                            expanded = false
                                            onContinuar()
                                        }) {
                                            Text(idioma.texto("usar"))
                                        }
                                    } else {
                                        Text(
                                            text = idioma.texto("soloPro"),
                                            color = Color.Red,
                                            fontSize = 14.sp,
                                            modifier = Modifier.padding(end = 12.dp)
                                        )
                                    }

                                    TextButton(onClick = { showDialog = true }) {
                                        Text(idioma.texto("eliminar"), color = Color.Red)
                                    }
                                }

                                if (showDialog) {
                                    AlertDialog(
                                        onDismissRequest = { showDialog = false },
                                        title = { Text(idioma.texto("eliminarCliente")) },
                                        text = {
                                            Text(
                                                idioma.texto("confirmarEliminar")
                                                    .replace("{nombre}", nombre)
                                            )
                                        },
                                        confirmButton = {
                                            TextButton(onClick = {

                                                val nuevaLista =
                                                    clientesGuardados.toMutableList().apply {
                                                        remove(nombre to celular)
                                                    }

                                                clientesGuardados = nuevaLista

                                                prefs.edit()
                                                    .putString(
                                                        "ListaClientes",
                                                        nuevaLista.joinToString("|") { it.first }
                                                    )
                                                    .remove("HistorialCalculadora_$nombre")
                                                    .remove("ULTIMA_LECTURA_FINAL_$nombre")
                                                    .remove("ULTIMA_FECHA_FINAL_$nombre")
                                                    .remove("LECTURA_INICIO_$nombre")
                                                    .remove("LECTURA_FIN_$nombre")
                                                    .remove("FECHA_INICIO_$nombre")
                                                    .remove("FECHA_FIN_$nombre")
                                                    .remove("COSTO_KWH_$nombre")
                                                    .apply()

                                                val clienteActual =
                                                    prefs.getString("NombreCliente", "") ?: ""
                                                if (clienteActual == nombre) {
                                                    prefs.edit()
                                                        .remove("NombreCliente")
                                                        .remove("CelularCliente")
                                                        .apply()
                                                }

                                                showDialog = false
                                            }) {
                                                Text(idioma.texto("eliminar"), color = Color.Red)
                                            }
                                        },
                                        dismissButton = {
                                            TextButton(onClick = { showDialog = false }) {
                                                Text(idioma.texto("cancelar"))
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    prefs.edit()
                        .putString("NombreCliente", nombreCliente)
                        .putString("CelularCliente", celularCliente)
                        .putString("CodigoPaisCliente", codigoPais)
                        .putString("MonedaCliente", moneda)   // ⭐ AGREGA ESTO
                        .apply()

                    val nuevoCliente = nombreCliente
                    val listaActual = prefs.getString("ListaClientes", "")!!
                        .split("|")
                        .filter { it.isNotBlank() }

                    if (nombreCliente.isNotBlank() && !listaActual.contains(nuevoCliente)) {
                        val nuevaLista = listaActual + nuevoCliente
                        prefs.edit().putString("ListaClientes", nuevaLista.joinToString("|"))
                            .apply()

                        clientesGuardados = nuevaLista.map { it to "" }
                    }

                    onContinuar()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(idioma.texto("continuar"))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 🔥 BANNER MOVIDO ARRIBA
            BannerAd(isPro = isPro.value)

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = idioma.texto("instruccion1") + "\n" +
                        idioma.texto("instruccion2") + "\n" +
                        idioma.texto("instruccion3"),
                fontSize = 15.sp,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(24.dp))
        }


//            ⭐⭐ MENÚ LATERAL SUPERPUESTO ⭐⭐--------------
        if (menuAbierto) {
            val idioma = LocalIdiomaViewModel.current
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x55000000))
                    .clickable { menuAbierto = false }
                    .zIndex(1f)
            )

            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.6f)
                    .background(MaterialTheme.colorScheme.background)
                    .border(1.dp, Color.LightGray)
                    .zIndex(2f)
            ) {

                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        // ✅ CAMBIO ÚNICO AQUÍ:
                        .verticalScroll(rememberScrollState())
                ) {

                    val seudonimoGuardado = prefs.getString("UserNickname", "") ?: ""

                    Text(
                        text = "👤 ${idioma.texto("usuario")}: $seudonimoGuardado",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        "🔄 " + idioma.texto("cambiarUsuario"),
                        fontSize = 16.sp,
                        color = Color(0xFF1E88E5),
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .clickable { mostrarDialogoCambio = true }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "🧑 " + idioma.texto("clienteTitulo"),
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    val cantidadClientes = clientesGuardados.size
                    Text(
                        text = when (cantidadClientes) {
                            0 -> idioma.texto("sinClientes")
                            1 -> idioma.texto("unCliente")
                            else -> idioma.texto("variosClientes").replace("{cantidad}", cantidadClientes.toString())
                        },
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                    )


                    Spacer(modifier = Modifier.height(16.dp))

                    if (!isPro.value) {
                        Text(
                            text = idioma.texto("mejorarPro"),
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.clickable { onIrPro() }
                        )
                    } else {
                        Text(
                            text = idioma.texto("cuentaProActiva"),
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    // AGREGA ESTE
                    Spacer(modifier = Modifier.height(16.dp))

                    var mostrarDialogoAyuda by remember { mutableStateOf(false) }
                    Text(
                        text = idioma.texto("ayudaContacto"),
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.clickable {
                            mostrarDialogoAyuda = true
                        }
                    )

                    if (mostrarDialogoAyuda) {
                        AlertDialog(
                            onDismissRequest = { mostrarDialogoAyuda = false },
                            title = { Text(idioma.texto("guiaUsoTitulo")) },
                            text = {
                                Column {
                                    Text(idioma.texto("guiaPasosTitulo"), fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text("1️⃣ " + idioma.texto("paso1"))
                                    Text("2️⃣ " + idioma.texto("paso2"))
                                    Text("3️⃣ " + idioma.texto("paso3"))
                                    Text("4️⃣ " + idioma.texto("paso4"))
                                    Text("5️⃣ " + idioma.texto("paso5"))
                                    Text("6️⃣ " + idioma.texto("paso6"))

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Text("💡 " + idioma.texto("mensajeConsejo"))
                                    Text("📞 " + idioma.texto("soporteTelefono"))
                                }
                            },
                            confirmButton = {
                                TextButton(onClick = { mostrarDialogoAyuda = false }) {
                                    Text(idioma.texto("btnAceptar"))
                                }
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    // ⭐ BOTÓN CALIFICAR APP (seguro, sin Billing)
                    val context = LocalContext.current

                    Text(
                        text = "⭐ " + idioma.texto("opinarApp"),
                        fontSize = 18.sp,
                        color = Color(0xFF1E88E5),
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .clickable {
                                val intent = Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("https://play.google.com/store/apps/details?id=com.nysae.calcu")
                                )
                                context.startActivity(intent)
                            }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    val themeViewModel = LocalThemeViewModel.current
                    val idioma = LocalIdiomaViewModel.current

                    var mostrarOpcionesConfig by remember { mutableStateOf(false) }

                    Text(
                        text = idioma.texto("configurar"),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .clickable { mostrarOpcionesConfig = !mostrarOpcionesConfig }
                    )

                    if (mostrarOpcionesConfig) {

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { themeViewModel.toggleTheme() }
                                .padding(vertical = 6.dp)
                        ) {
                            Text(
                                text = if (themeViewModel.isDarkTheme.value)
                                    idioma.texto("modoOscuro")
                                else
                                    idioma.texto("modoClaro"),
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.weight(1f)
                            )

                            Switch(
                                checked = themeViewModel.isDarkTheme.value,
                                onCheckedChange = { themeViewModel.toggleTheme() }
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { idioma.cambiarIdioma() }
                                .padding(vertical = 6.dp)
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
                                checked = !idioma.idiomaEs,   // 🔄 invertido
                                onCheckedChange = { idioma.cambiarIdioma() }
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    Spacer(modifier = Modifier.weight(1f)) // Ocupa todo el espacio restante

                    Button(
                        onClick = { menuAbierto = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(idioma.texto("cerrarMenu"))
                    }
                } // ← Cierra Column
            } // ← Cierra Box del menú
        } // ← Cierra if(menuAbierto)

//---------------------------------------------------------------------
// ⭐⭐ DIÁLOGO CAMBIAR USUARIO ⭐⭐
//---------------------------------------------------------------------
        if (mostrarDialogoCambio) {

            var nuevoNombre by remember { mutableStateOf("") }

            AlertDialog(
                onDismissRequest = { mostrarDialogoCambio = false },
                title = { Text(idioma.texto("cambiarUsuario")) },
                text = {
                    Column {
                        Text(idioma.texto("escribeSeudonimo"))
                        TextField(
                            value = nuevoNombre,
                            onValueChange = { nuevoNombre = it }
                        )
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        prefs.edit().putString("UserNickname", nuevoNombre).apply()
                        mostrarDialogoCambio = false
                    }) {
                        Text(idioma.texto("guardar"))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { mostrarDialogoCambio = false }) {
                        Text(idioma.texto("cancelar"))
                    }
                }
            )
        }
    } // Cierra Column principal
} // Cierra Box principal
