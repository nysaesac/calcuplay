package com.nysae.calcu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.compositionLocalOf   // ⭐ FALTABA
import androidx.lifecycle.viewmodel.compose.viewModel  // ⭐ FALTABA
import com.nysae.ui.MedidorConsumoTheme
import android.content.SharedPreferences
import com.google.android.gms.ads.MobileAds
import com.nysae.ui.ThemeViewModel
import com.nysae.ui.LocalThemeViewModel

val LocalMainActivity = compositionLocalOf<MainActivity> { error("No MainActivity") }


class MainActivity : ComponentActivity() {

    private val PREFS_NAME = "NysaePrefs"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✔ Inicializar AdMob
        MobileAds.initialize(this)
        // ✔ Cargar anuncios recompensados
        CreditosAdsManager.cargar(this)
        // ⭐ PRUEBA credito : dar 3 créditos
        val prefs = getSharedPreferences("NysaePrefs", MODE_PRIVATE)

        val primeraVez = prefs.getBoolean("PRIMERA_VEZ_CREDITOS", true)

        if (primeraVez) {
            CreditosManager.agregarCredito(prefs, 3)
            prefs.edit().putBoolean("PRIMERA_VEZ_CREDITOS", false).apply()
        }
           // ⭐⭐⭐ MODIFICACIÓN AGREGADA: CONTROL DE VERSION ⭐⭐⭐
        VersionManager.checkUpdate(this) {
            showUpdateDialog()
        }
        setContent {

            // ⭐ Crear ViewModel para el tema
            val themeViewModel: ThemeViewModel = viewModel()
            // ⭐ Crear ViewModel para el idioma
            val idiomaViewModel: IdiomaViewModel = viewModel()

            // ⭐ Aplicar tema (claro u oscuro)
            MedidorConsumoTheme(
                darkTheme = themeViewModel.isDarkTheme.value
            ) {

                // ⭐ Habilitar acceso global al MainActivity y al ThemeViewModel
                CompositionLocalProvider(
                    LocalMainActivity provides this,
                    LocalThemeViewModel provides themeViewModel,
                    LocalIdiomaViewModel provides idiomaViewModel  // ✅ Aquí agregamos el idioma
                ) {

                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {

                        val context = LocalContext.current
                        val prefs = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE)

                        // ⭐ Estado Pro reactivo
                        var isPro by remember {
                            mutableStateOf(prefs.getBoolean("PREF_IS_PRO", false))
                        }

                        // ⭐ Verificar si ya activó la app por primera vez
                        val activated = prefs.getBoolean("Activated", false)

                        // ⭐ Pantalla correspondiente
                        var currentScreen by remember {
                            mutableStateOf(
                                if (activated) "Cliente" else "Activation"
                            )
                        }

                        // =====================================
                        // NAVEGACIÓN ENTRE PANTALLAS
                        // =====================================
                        when (currentScreen) {

                            "Activation" -> ActivationScreen(
                                prefs = prefs,
                                onActivated = {
                                    // Guardar que activó la app
                                    prefs.edit().putBoolean("Activated", true).apply()
                                    currentScreen = "Cliente"
                                },
                                onIrPro = {
                                    // Ir a pantalla PRO
                                    currentScreen = "Pro"
                                }
                            )

                            "Cliente" -> ClienteScreen(
                                prefs = prefs,
                                isPro = derivedStateOf { isPro },
                                onContinuar = { currentScreen = "Calculadora" },
                                onIrPro = { currentScreen = "Pro" }
                            )

                            "Calculadora" -> CalculadoraScreen(
                                prefs = prefs,
                                isPro = derivedStateOf { isPro },
                                onVerHistorial = { currentScreen = "Historial" },
                                onVolver = { currentScreen = "Cliente" },
                                onIrPro = { currentScreen = "Pro" }
                            )

                            "Historial" -> HistorialScreen(
                                prefs = prefs,
                                isPro = derivedStateOf { isPro },
                                onVolver = { currentScreen = "Calculadora" },
                                onIrPro = { currentScreen = "Pro" }
                            )

                            // ⭐⭐⭐ AGREGADO - PANTALLA PRO ⭐⭐⭐
                            "Pro" -> PantallaPro(
                                prefs = prefs,
                                isProState = derivedStateOf { isPro },
                                onProActivado = {
                                    prefs.edit().putBoolean("PREF_IS_PRO", true).apply()
                                    isPro = true
                                    currentScreen = "Cliente"
                                },
                                onVolver = { currentScreen = "Cliente" }
                            )
                        }
                    }
                }
            }
        }
    }

    // ⭐⭐⭐ MODIFICACIÓN AGREGADA: DIALOG DE ACTUALIZACIÓN ⭐⭐⭐
    fun showUpdateDialog() {

        android.app.AlertDialog.Builder(this)
            .setTitle("Actualizar app / Update app")
            .setMessage("Debes actualizar para continuar.\nYou must update to continue.")
            .setCancelable(false)
            .setPositiveButton("Actualizar / Update") { _, _ ->

                val intent = android.content.Intent(
                    android.content.Intent.ACTION_VIEW,
                    android.net.Uri.parse(
                        "https://play.google.com/store/apps/details?id=com.nysae.calcu"
                    )
                )
                startActivity(intent)
            }
            .setNegativeButton("Salir / Exit") { _, _ ->
                finish()
            }
            .show()
    }
    // ⭐⭐⭐ FIN MODIFICACIÓN ⭐⭐⭐
}


// ============================
// FUNCIONES DE HISTORIAL
// ============================

fun guardarHistorial(prefs: SharedPreferences, nombreCliente: String, resultado: String) {
    val key = "HistorialCalculadora_${nombreCliente.ifBlank { "General" }}"
    val historialStr = prefs.getString(key, "") ?: ""
    val historialList =
        if (historialStr.isNotEmpty()) historialStr.split("|").toMutableList() else mutableListOf()
    val nuevo = resultado
    historialList.remove(nuevo)
    historialList.add(0, nuevo)
    if (historialList.size > 50) historialList.removeLast()
    prefs.edit().putString(key, historialList.joinToString("|")).apply()
}

fun leerHistorial(prefs: SharedPreferences, nombreCliente: String): List<String> {
    val key = "HistorialCalculadora_${nombreCliente.ifBlank { "General" }}"
    val historialStr = prefs.getString(key, "") ?: ""
    return if (historialStr.isEmpty()) emptyList() else historialStr.split("|")
}
