package com.nysae.calcu

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.*

class BillingClientManager(
    private val context: Context,
    private val onProStateChanged: (Boolean) -> Unit
) : PurchasesUpdatedListener {

    private var billingClient: BillingClient = BillingClient.newBuilder(context)
        .enablePendingPurchases()
        .setListener(this)
        .build()

    fun startConnection() {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    checkActiveSubscriptions()
                }
            }

            override fun onBillingServiceDisconnected() {
                startConnection()
            }
        })
    }

    fun checkActiveSubscriptions() {
        billingClient.queryPurchasesAsync(
            QueryPurchasesParams.newBuilder()
                .setProductType(BillingClient.ProductType.SUBS)
                .build()
        ) { billingResult, purchases ->

            var isPro = false

            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                for (purchase in purchases) {
                    // ✅ SOLO SE VERIFICA EL ID DE LA SUSCRIPCIÓN
                    if (purchase.products.contains("pro")) {
                        isPro = true
                    }
                }
            }

            onProStateChanged(isPro)
        }
    }

    // ✅ FIRMA CORREGIDA
    fun comprarSuscripcion(
        activity: Activity,
        productId: String,
        basePlanId: String
    ) {
        val productList = listOf(
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(productId) // "pro"
                .setProductType(BillingClient.ProductType.SUBS)
                .build()
        )

        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(productList)
            .build()

        billingClient.queryProductDetailsAsync(params) { _, productDetailsList ->

            val product = productDetailsList.firstOrNull()
                ?: return@queryProductDetailsAsync

            // ✅ SELECCIONA EL PLAN CORRECTO (monthly / yearly)
            val offer = product.subscriptionOfferDetails
                ?.firstOrNull { it.basePlanId == basePlanId }
                ?: return@queryProductDetailsAsync

            val billingParams = BillingFlowParams.newBuilder()
                .setProductDetailsParamsList(
                    listOf(
                        BillingFlowParams.ProductDetailsParams.newBuilder()
                            .setProductDetails(product)
                            .setOfferToken(offer.offerToken)
                            .build()
                    )
                )
                .build()

            billingClient.launchBillingFlow(activity, billingParams)
        }
    }

    override fun onPurchasesUpdated(
        billingResult: BillingResult,
        purchases: MutableList<Purchase>?
    ) {
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            for (purchase in purchases) {
                if (purchase.products.contains("pro")) {
                    onProStateChanged(true)
                }
            }
        }
    }
}

