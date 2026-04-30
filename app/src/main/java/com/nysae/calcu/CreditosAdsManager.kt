package com.nysae.calcu

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

object CreditosAdsManager {

    private var rewardedAd: RewardedAd? = null

    private const val AD_ID = "ca-app-pub-2143110449423705/6820263672"

    // ===================== CARGAR =====================
    fun cargar(context: Context) {
        val request = AdRequest.Builder().build()

        RewardedAd.load(
            context,
            AD_ID,
            request,
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {
                    rewardedAd = ad
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    rewardedAd = null
                }
            }
        )
    }

    // ===================== MOSTRAR =====================
    fun mostrar(
        activity: Activity,
        prefs: SharedPreferences,
        onReward: (() -> Unit)? = null
    ) {
        if (rewardedAd != null) {

            rewardedAd?.show(activity) {
                // 🎯 DAR CRÉDITO
                CreditosManager.agregarCredito(prefs, 2)

                // 🔥 NOTIFICAR A LA UI (ACTUALIZACIÓN INMEDIATA)
                onReward?.invoke()

                // recargar anuncio
                cargar(activity)
            }

        } else {
            cargar(activity)
        }
    }
}