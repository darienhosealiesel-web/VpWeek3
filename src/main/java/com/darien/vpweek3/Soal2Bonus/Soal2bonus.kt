package com.darien.vpweek3

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.OutlinedTextFieldDefaults.colors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

val Gelap = Color(0xFF0F1426)
val KartuGelap = Color(0xFF1B2238)
val Biru = Color(0xFF5DB8F5)
val Abu = Color(0xFF9AA6C4)

@Preview(showBackground = true)
@Composable
fun Soal2BonusPreview() {
    Soal2BonusView()
}

@Composable
fun Soal2BonusView(modifier: Modifier = Modifier) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var spot by remember { mutableStateOf("Fjellheisen Cable Car") }
    var pengalaman by remember { mutableStateOf("Pemandangan langit malam hijau aurora spektakuler dari puncak gunung...") }
    var catatan by remember { mutableStateOf("") }
    var kepuasan by remember { mutableStateOf("Luar Biasa ★") }

    val warnaField = colors(
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        cursorColor = Biru,
        focusedContainerColor = KartuGelap,
        unfocusedContainerColor = KartuGelap,
        focusedBorderColor = Biru,
        unfocusedBorderColor = Abu,
        focusedLabelColor = Biru,
        unfocusedLabelColor = Abu,
        focusedPlaceholderColor = Abu,
        unfocusedPlaceholderColor = Abu
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Gelap)
                .imePadding()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(listOf(Color(0xFF1E3A8A), Color(0xFF3B6FD4))),
                        RoundedCornerShape(12.dp)
                    )
                    .padding(16.dp)
            ) {
                Text(
                    "LOG PERJALANAN",
                    color = Color(0xFFCFE0FF),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Tromsø, Norway ❄",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text("Ekspedisi Aurora Borealis", color = Color.White, fontSize = 12.sp)
            }

            OutlinedTextField(
                value = spot,
                onValueChange = { spot = it },
                label = { Text("SPOT WISATA FAVORIT") },
                singleLine = true,
                colors = warnaField,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = pengalaman,
                onValueChange = { pengalaman = it },
                label = { Text("APA YANG PALING KAMU NIKMATI?") },
                minLines = 3,
                colors = warnaField,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = catatan,
                onValueChange = { catatan = it },
                label = { Text("CATATAN TAMBAHAN / PERLENGKAPAN") },
                placeholder = { Text("(Ketik perlengkapan ekstra di sini...)") },
                colors = warnaField,
                modifier = Modifier.fillMaxWidth()
            )

            Text("TINGKAT KEPUASAN:", color = Abu, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Biasa", "Seru", "Luar Biasa ★").forEach { pilihan ->
                    val dipilih = kepuasan == pilihan
                    Button(
                        onClick = { kepuasan = pilihan },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (dipilih) Biru else KartuGelap,
                            contentColor = if (dipilih) Gelap else Color.White
                        )
                    ) {
                        Text(pilihan, fontSize = 12.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Row {
                Text("Status: ", color = Abu, fontSize = 12.sp)
                Text(
                    "Draft Tersimpan",
                    color = Biru,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        FloatingActionButton(
            onClick = {
                val teksTampil =
                    "Jurnal tersimpan! Spot favorit: ${spot.ifBlank { "(belum diisi)" }}"
                scope.launch {
                    snackbarHostState.showSnackbar(teksTampil)
                }
            },
            containerColor = Biru,
            contentColor = Gelap,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .navigationBarsPadding()
                .padding(16.dp)
        ) {
            Text("+", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(bottom = 88.dp)
        )
    }
}