package com.nysae.calcu

import android.content.Context
import kotlinx.coroutines.*
import java.net.URL

object VersionManager {

    fun checkUpdate(context: Context, onUpdate: () -> Unit) {

        CoroutineScope(Dispatchers.IO).launch {
            try {

                val url = URL("https://nysaesac.github.io/calcu_version.txt")
                val text = url.readText().trim()

                val parts = text.split("=")
                if (parts.size != 2) return@launch

                val minVersion = parts[1].trim().toIntOrNull()
                    ?: return@launch

                // ⭐ REEMPLAZO SEGURO (SIN BUILDCONFIG)
                val versionApp = context.packageManager
                    .getPackageInfo(context.packageName, 0)
                    .longVersionCode.toInt()

                if (versionApp < minVersion) {
                    withContext(Dispatchers.Main) {
                        onUpdate()
                    }
                }

            } catch (e: Exception) {
                // no bloquea app
            }
        }
    }
}