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

    if (!isPro) {  // <- SOLO muestra banner si el usuario NO es Pro
        val context = LocalContext.current

        AndroidView(
            modifier = modifier,
            factory = {
                AdView(context).apply {
                    // Banner de real
                    adUnitId = "ca-app-pub-2143110449423705/3640857561"
                    setAdSize(AdSize.BANNER)
                    loadAd(AdRequest.Builder().build())
                }
            }
        )
    }
}
