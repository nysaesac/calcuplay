package com.nysae.calcu

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@Composable
fun BannerAd(isPro: Boolean, modifier: Modifier = Modifier) {

    if (!isPro) {
        val context = LocalContext.current

        AndroidView(
            modifier = modifier,
            factory = {
                val adView = AdView(context)

                // 🔥 CALCULAR ANCHO REAL
                val density = context.resources.displayMetrics.density
                val adWidthPixels = context.resources.displayMetrics.widthPixels
                val adWidth = (adWidthPixels / density).toInt()

                // 🔥 TAMAÑO ADAPTATIVO (IGUAL QUE TU OTRA APP)
                val adSize = AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(
                    context,
                    adWidth
                )

                adView.setAdSize(adSize)

                // ID DE ESTA APP (CORRECTO, NO CAMBIAR)
                adView.adUnitId = "ca-app-pub-2143110449423705/3640857561"

                adView.loadAd(AdRequest.Builder().build())

                adView
            }
        )
    }
}