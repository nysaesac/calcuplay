package com.nysae.calcu

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Cabecera() {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFB8B8B8))
            .border(2.dp, Color(0xFFFFD700), RoundedCornerShape(6.dp))
            .padding(vertical = 8.dp, horizontal = 12.dp)
    ) {

        // ==============================
        //  CONTENIDO CENTRAL DE CABECERA
        // ==============================
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {

                Icon(
                    Icons.Default.FlashOn,
                    contentDescription = "Rayo",
                    tint = Color(0xFFFFC107),
                    modifier = Modifier.size(28.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(color = Color(0xFF2196F3), fontWeight = FontWeight.Bold)) {
                            append("NY")
                        }
                        withStyle(SpanStyle(color = Color.Red, fontWeight = FontWeight.Bold)) {
                            append("SAE ")
                        }
                        withStyle(SpanStyle(color = Color.Black, fontWeight = FontWeight.Bold)) {
                            append("S.A.C.")
                        }
                    },
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.width(8.dp))

                Icon(
                    Icons.Default.FlashOn,
                    contentDescription = "Rayo",
                    tint = Color(0xFFFFC107),
                    modifier = Modifier.size(28.dp)
                )
            }

            // ==============================
            //  WhatsApp — bonito, pequeño
            // ==============================
            Box(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .background(Color(0xFFEFFFEF), RoundedCornerShape(6.dp))
                    .border(1.dp, Color(0xFF25D366), RoundedCornerShape(6.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "💬 WhatsApp: +51 954 022 926",
                    fontSize = 11.sp,
                    color = Color(0xFF1B5E20),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}