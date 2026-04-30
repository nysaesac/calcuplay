package com.nysae.calcu

import android.content.SharedPreferences

object CreditosManager {

    private const val KEY_CREDITOS = "CREDITOS"
    private const val KEY_CALCULOS = "CALCULOS_USADOS"

    // ===================== CRÉDITOS =====================
    fun obtenerCreditos(prefs: SharedPreferences): Int {
        return prefs.getInt(KEY_CREDITOS, 0)
    }

    fun agregarCredito(prefs: SharedPreferences, cantidad: Int = 1) {
        val actuales = obtenerCreditos(prefs)
        prefs.edit().putInt(KEY_CREDITOS, actuales + cantidad).apply()
    }

    fun usarCredito(prefs: SharedPreferences): Boolean {
        val actuales = obtenerCreditos(prefs)
        return if (actuales > 0) {
            prefs.edit().putInt(KEY_CREDITOS, actuales - 1).apply()
            true
        } else false
    }

    // ===================== CÁLCULOS =====================
    fun obtenerCalculos(prefs: SharedPreferences): Int {
        return prefs.getInt(KEY_CALCULOS, 0)
    }

    fun sumarCalculo(prefs: SharedPreferences): Int {
        val nuevos = obtenerCalculos(prefs) + 1
        prefs.edit().putInt(KEY_CALCULOS, nuevos).apply()
        return nuevos
    }

    fun resetCalculos(prefs: SharedPreferences) {
        prefs.edit().putInt(KEY_CALCULOS, 0).apply()
    }
}