package com.nysae.calcu

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class IdiomaViewModel : ViewModel() {
    // true = español, false = inglés
    var idiomaEs by mutableStateOf(true)
        private set

    fun cambiarIdioma() {
        idiomaEs = !idiomaEs
    }

    fun setIdioma(es: Boolean) {
        idiomaEs = es
    }

    // =====================================
    // TEXTOS EN ESPAÑOL / INGLÉS
    // PANTALLA CLIENTE
    // =====================================
    fun texto(key: String): String {

        val es = mapOf(
            // --- Pantalla activación ---
            "ingreseSeudonimo" to "Ingrese su seudónimo",
            "bienvenidaActivacion" to "Bienvenido, activa tu aplicación calculadora de consumo",
            "btnIngresar" to "Ingresar",
            "errorSeudonimoObligatorio" to "Debe ingresar un seudónimo.",
            "actualizacion_titulo" to "Actualización requerida",
            "actualizacion_mensaje" to "Existe una nueva versión de la app. Debes actualizar para continuar.",
            "actualizar" to "Actualizar",
            "salir" to "Salir",

            // --- Pantalla Cliente ---
            "bienvenida" to "Bienvenido",
            "ingresaDatosCliente" to "ingresa los datos de tu cliente",
            "nombreCliente" to "Nombre del cliente",
            "codigo" to "Código",
            "celular" to "Celular",
            "seleccionMoneda" to "SELECCIÓN DE MONEDA",
            "monedaCalculos" to "Moneda usada en cálculos",
            "clientesRegistrados" to "Clientes registrados",
            "usar" to "Usar",
            "soloPro" to "Solo PRO 🔒",
            "eliminar" to "Eliminar",
            "eliminarCliente" to "Eliminar cliente",
            "confirmarEliminar" to "¿Seguro que deseas eliminar \"{nombre}\"?",
            "cancelar" to "Cancelar",
            "continuar" to "Continuar",
            "instruccion1" to "1️⃣ Registra los datos del equipo: voltaje, amperaje, precio del kWh y tiempo de uso.",
            "instruccion2" to "2️⃣ Presiona CALCULAR para obtener el consumo y el costo total.",
            "instruccion3" to "3️⃣ Todos los cálculos quedan guardados automáticamente para cada cliente.",

            "usuario" to "Usuario",
            "cambiarUsuario" to "Cambiar usuario",
            "clienteTitulo" to "Cliente",
            "sinClientes" to "No hay clientes registrados",
            "unCliente" to "1 cliente registrado",
            "variosClientes" to "{cantidad} clientes registrados",
            "mejorarPro" to "Mejorar a PRO 💎",
            "cuentaProActiva" to "💎 Cuenta PRO activa",
            "ayudaContacto" to "❓ Ayuda / Contacto",
            "opinarApp" to "Opinar app",

            // --- Ventana de ayuda ---
            "guiaUsoTitulo" to "Guía de Uso",
            "guiaPasosTitulo" to "📌 Pasos para usar la app:",
            "paso1" to "Ingresa el nombre del cliente.",
            "paso2" to "Ingresa código de país y número de celular.",
            "paso3" to "Selecciona la moneda para cálculos.",
            "paso4" to "Guarda clientes y accede a ellos cuando necesites continuar.",
            "paso5" to "Ingresa los datos del equipo: voltaje, amperaje, horas de uso y precio del kWh.",
            "paso6" to "Consulta el consumo calculado y revisa el historial por cliente.",
            "mensajeConsejo" to "Administra y controla fácilmente el consumo eléctrico de cada equipo.",
            "soporteTelefono" to "Soporte: +51 954022926",
            "btnAceptar" to "Aceptar",

            // --- Configuración ---
            "configurar" to "⚙ Configurar",
            "modoOscuro" to "🌙 Modo oscuro",
            "modoClaro" to "☀️ Modo claro",
            "idiomaEspanol" to "🇪🇸 Idioma: Español",
            "idiomaIngles" to "🇺🇸 Idioma: Inglés",
            "cerrarMenu" to "Cerrar menú",

            // --- Cambio de usuario ---
            "escribeSeudonimo" to "Escribe el nuevo seudónimo:",
            "guardar" to "Guardar",

            // --- Botón PRO ---
            "graciasPro" to "💎 Gracias por ser PRO",

            // --- Pantalla PRO ---
            "proTitulo" to "Mejorar a Modo PRO",
            "proSinAnuncios" to "Sin anuncios",
            "proHistorialIlimitado" to "Historial ilimitado",
            "proFuncionesExclusivas" to "Funciones exclusivas",
            "proMejorRendimiento" to "Mejor rendimiento",
            "proSuscripcionMensual" to "Suscribirse Mensual",
            "proSuscripcionAnual" to "Suscribirse Anual",
            "proYaEresPro" to "💎 Ya eres PRO",
            "proVolver" to "Volver",

            // --- Pantalla calculadora ---
            "snackbarValoresModificados" to "⚠️ Se modificaron valores. Presione CALCULAR 🧮 NYSAE para actualizar.",
            "cambiarCliente" to "Cambiar cliente",
            "clienteSinAsignar" to "Sin asignar",
            "nombreEquipo" to "Nombre del equipo",
            "amp" to "Amperios (A)",
            "ma" to "Miliamperios (mA)",
            "horas" to "Horas",
            "minutos" to "Minutos",
            "costoKwh" to "Precio del kWh",
            "voltaje" to "Voltaje (V)",
            "creditosDisponibles" to "Créditos disponibles:",
            "verVideoCreditos" to "Ver video y ganar créditos",
            "sinCreditos" to "Sin créditos. Mira un video para continuar.",
            "calculoRepetido" to "Ya calculaste con los mismos datos",


            // --- Snackbars / avisos ---
            "completarCampos" to "⚠️ Completar todos los campos",
            "modoTemporal" to "⚠️ Modo temporal: sin cliente, no se guarda historial.",
            "datosYaGuardados" to "📂 Los datos ya están guardados en el historial.",

            // --- Botón calcular y estado ---
            "calcularBtn" to "CALCULAR 🧮 NYSAE",
            "datosGuardadosBtn" to "💾 Datos guardados",
            "bannerPro" to "⭐ Banner PRO",

            // --- Resultados (tarjeta / labels) ---
            "resumenTitulo" to "📊 Resultado de",
            "resumenFecha" to "📅 Fecha",
            "potenciaW" to "Potencia W",
            "potenciaKW" to "Potencia kW",
            "kwhDia" to "kWh por día",
            "kwhMes" to "kWh por mes",
            "costoDia" to "Costo por día",
            "costoMes" to "Costo por mes",

            // --- Botón historial ---
            "verHistorialRecibos" to "Ver historial de recibos",
            // --- pantalla historial ---
            "historialTituloConNombre" to "Historial de Recibos %s",
            "historialTituloSinNombre" to "Historial de Recibos (sin asignar)",
            "historialAcciones" to "Acciones: compartir, borrar uno o borrar todo",
            "soloUsuariosPro" to "Función disponible solo para usuarios PRO",
            "seleccionRegistros" to "☑ Selecciona registros para enviar solo esos.\n⚡ Si no seleccionas nada, se enviará todo.",

            "volverBtn" to "Volver",
            "asignarClientePrimero" to "Primero asigne un cliente para ver historial",
            "enviarWhatsappBtn" to "Enviar WhatsApp",
            "historialVacio" to "No hay historial para enviar",
            "faltaCodigoNumero" to "Falta código o número",
            "borrarTodoTitulo" to "Confirmar borrado",
            "borrarTodoTexto" to "¿Seguro que deseas borrar todo el historial y las lecturas guardadas?",
            "siBorrarTodo" to "Sí, borrar todo",
            "historialEliminado" to "Historial y lecturas eliminados correctamente",
            "borrarUnoTitulo" to "Confirmar borrado",
            "borrarUnoTexto" to "¿Seguro que deseas eliminar este registro?",
            "eliminarRegistro" to "Eliminar",
            "borrarEste" to "Borrar este"
        )

        val en = mapOf(
            // --- Activation screen ---
            "ingreseSeudonimo" to "Enter your nickname",
            "bienvenidaActivacion" to "Welcome, activate your consumption calculator app",
            "btnIngresar" to "Enter",
            "errorSeudonimoObligatorio" to "You must enter a nickname.",
            "actualizacion_titulo" to "Update required",
            "actualizacion_mensaje" to "A new version of the app is available. You must update to continue.",
            "actualizar" to "Update",
            "salir" to "Exit",

            // --- Client screen ---
            "bienvenida" to "Welcome",
            "ingresaDatosCliente" to "enter your client's information",
            "nombreCliente" to "Client name",
            "codigo" to "code",
            "celular" to "Phone",
            "seleccionMoneda" to "CURRENCY SELECTION",
            "monedaCalculos" to "Currency used for calculations",
            "clientesRegistrados" to "Registered clients",
            "usar" to "Use",
            "soloPro" to "PRO only 🔒",
            "eliminar" to "Delete",
            "eliminarCliente" to "Delete client",
            "confirmarEliminar" to "Are you sure you want to delete \"{nombre}\"?",
            "cancelar" to "Cancel",
            "continuar" to "Continue",
            "instruccion1" to "1️⃣ Enter the equipment data: voltage, amperage, kWh price, and usage time.",
            "instruccion2" to "2️⃣ Press CALCULATE to obtain the consumption and total cost.",
            "instruccion3" to "3️⃣ All calculations are automatically saved for each client.",
            "usuario" to "User",
            "cambiarUsuario" to "Change user",
            "clienteTitulo" to "Client",
            "sinClientes" to "No registered clients",
            "unCliente" to "1 registered client",
            "variosClientes" to "{cantidad} registered clients",
            "mejorarPro" to "Upgrade to PRO 💎",
            "cuentaProActiva" to "💎 PRO account active",
            "ayudaContacto" to "❓ Help / Contact",
            "opinarApp" to "Rate app",

            // --- Help dialog ---
            "guiaUsoTitulo" to "User Guide",
            "guiaPasosTitulo" to "📌 Steps to use the app:",
            "paso1" to "Enter the client's name.",
            "paso2" to "Enter the country code and phone number.",
            "paso3" to "Select the currency for calculations.",
            "paso4" to "Save clients and access them whenever you need to continue.",
            "paso5" to "Enter the equipment data: voltage, amperage, usage hours, and kWh price.",
            "paso6" to "Check the calculated consumption and review the client history.",
            "mensajeConsejo" to "Manage and track the electrical consumption of each device easily.",
            "soporteTelefono" to "Support: +51 954022926",
            "btnAceptar" to "Accept",

            // --- Settings ---
            "configurar" to "⚙ Settings",
            "modoOscuro" to "🌙 Dark mode",
            "modoClaro" to "☀️ Light mode",
            "idiomaEspanol" to "🇪🇸 Language: Spanish",
            "idiomaIngles" to "🇺🇸 Language: English",
            "cerrarMenu" to "Close menu",

            // --- Change user ---
            "escribeSeudonimo" to "Enter the new nickname:",
            "guardar" to "Save",

            // --- Botón PRO ---
            "graciasPro" to "💎 Thanks for being PRO",

            // --- PRO Screen ---
            "proTitulo" to "Upgrade to PRO Mode",
            "proSinAnuncios" to "No ads",
            "proHistorialIlimitado" to "Unlimited history",
            "proFuncionesExclusivas" to "Exclusive features",
            "proMejorRendimiento" to "Better performance",
            "proSuscripcionMensual" to "Subscribe Monthly",
            "proSuscripcionAnual" to "Subscribe Yearly",
            "proYaEresPro" to "💎 You are already PRO",
            "proVolver" to "Back",

            // --- Calculator screen ---
            "snackbarValoresModificados" to "⚠️ Values were modified. Press CALCULATE 🧮 NYSAE to update.",
            "cambiarCliente" to "Change client",
            "clienteSinAsignar" to "Unassigned",
            "nombreEquipo" to "Device name",
            "amp" to "Amperes (A)",
            "ma" to "Milliamps (mA)",
            "horas" to "Hours",
            "minutos" to "Minutes",
            "costoKwh" to "kWh price",
            "voltaje" to "Voltage (V)",
            "creditosDisponibles" to "Credits available:",
            "verVideoCreditos" to "Watch video and earn credits",
            "sinCreditos" to "No credits. Watch a video to continue.",
            "calculoRepetido" to "You already calculated with the same data",

                // --- Snackbars / avisos ---
            "completarCampos" to "⚠️ Fill in all fields",
            "modoTemporal" to "⚠️ Temporary mode: no client, history not saved.",
            "datosYaGuardados" to "📂 Data already saved in history.",

            // --- Botón calcular y estado ---
            "calcularBtn" to "CALCULATE 🧮 NYSAE",
            "datosGuardadosBtn" to "💾 Data saved",
            "bannerPro" to "⭐ PRO Banner",

            // --- Resultados (tarjeta / labels) ---
            "resumenTitulo" to "📊 Result of",
            "resumenFecha" to "📅 Date",
            "potenciaW" to "Power W",
            "potenciaKW" to "Power kW",
            "kwhDia" to "kWh per day",
            "kwhMes" to "kWh per month",
            "costoDia" to "Cost per day",
            "costoMes" to "Cost per month",

            // --- Botón historial ---
            "verHistorialRecibos" to "View Receipts History",
            // --- pantalla historial ---
            "historialTituloConNombre" to "Receipt History %s",
            "historialTituloSinNombre" to "Receipt History (unassigned)",
            "historialAcciones" to "Actions: share, delete one or delete all",
            "soloUsuariosPro" to "Feature available only for PRO users",
            "seleccionRegistros" to "☑ Select records to send only those.\n⚡ If none are selected, everything will be sent.",
            "volverBtn" to "Back",
            "asignarClientePrimero" to "Assign a client first to view history",
            "enviarWhatsappBtn" to "Send WhatsApp",
            "historialVacio" to "There is no history to send",
            "faltaCodigoNumero" to "Country code or phone number missing",
            "borrarTodoTitulo" to "Confirm deletion",
            "borrarTodoTexto" to "Are you sure you want to delete all history and saved readings?",
            "siBorrarTodo" to "Yes, delete all",
            "historialEliminado" to "History and readings deleted successfully",
            "borrarUnoTitulo" to "Confirm deletion",
            "borrarUnoTexto" to "Are you sure you want to delete this record?",
            "eliminarRegistro" to "Delete",
            "borrarEste" to "Delete this"
        )


        return if (idiomaEs) es[key] ?: key else en[key] ?: key
    }
}