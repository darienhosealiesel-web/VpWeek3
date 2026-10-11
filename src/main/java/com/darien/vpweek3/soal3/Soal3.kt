package com.darien.vpweek3.soal3

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.random.Random

enum class GameState {
    START, COUNTDOWN, RUNNING, GAME_OVER
}
enum class Mode {
    COLOR, TEXT
}
enum class Choice {
    WORD_COLOUR, WORD
}
enum class ColorName(val label: String, val warna: Color) {
    RED("RED", Color(0xFFF44336)),
    BLUE("BLUE", Color(0xFF2196F3)),
    GREEN("GREEN", Color(0xFF4CAF50)),
    ORANGE("ORANGE", Color(0xFFFF9800))
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun Soal3Preview() {
    Soal3View()
}

@Composable
fun Soal3View(modifier: Modifier = Modifier) {
    var state by remember { mutableStateOf(GameState.START) }
    var countdown by remember { mutableStateOf("3") }
    var skor by remember { mutableIntStateOf(0) }
    var salah by remember { mutableIntStateOf(0) }
    var best by remember { mutableIntStateOf(0) }

    var mode by remember { mutableStateOf(Mode.COLOR) }
    var kata by remember { mutableStateOf(ColorName.RED) }
    var tinta by remember { mutableStateOf(ColorName.BLUE) }
    var inkleft by remember { mutableStateOf(true) }
    var sisaWaktu by remember { mutableIntStateOf(5000) }
    var soalKe by remember { mutableIntStateOf(0) }

    fun soalBaru() {
        mode = Mode.entries.random()
        kata = ColorName.entries.random()
        tinta = ColorName.entries.filter { it != kata }.random()
        inkleft = Random.nextBoolean()
        sisaWaktu = 5000
        soalKe++
    }

    fun mulai() {
        skor = 0
        salah = 0
        state = GameState.COUNTDOWN
    }

    fun jawab(benar: Boolean) {
        if (benar) {
            skor++
        } else {
            salah++
            if (salah >= 3) {
                if (skor > best) {
                    best = skor
                }
                state = GameState.GAME_OVER
                return
            }
        }
        soalBaru()
    }

    LaunchedEffect(state) {
        if (state == GameState.COUNTDOWN) {
            listOf("3", "2", "1").forEach {
                    angka ->
                countdown = angka
                delay(1000)
            }
            countdown = "Start!"
            delay(700)
            soalBaru()
            state = GameState.RUNNING
        }
    }

    LaunchedEffect(soalKe, state) {
        if (state == GameState.RUNNING) {
            while (sisaWaktu > 0) {
                delay(100)
                sisaWaktu -= 100
            }
            jawab(false)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (state) {
            GameState.START -> {
                Text(
                    "Welcome\nto\nColor Word Matching",
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(32.dp))
                Button(onClick = { mulai() }) { Text("Start Game") }
            }

            GameState.COUNTDOWN -> {
                Text(countdown, fontSize = 32.sp)
            }

            GameState.RUNNING -> {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Mode: ${mode.name}")
                    Text("✅ $skor  ❌ $salah/3")
                }

                Spacer(Modifier.height(12.dp))
                Text(
                    if (mode == Mode.COLOR) "Choose Colour" else "Choose Word",
                    fontSize = 14.sp
                )

                Spacer(Modifier.height(32.dp))
                Text("${(sisaWaktu + 999) / 1000} s", fontSize = 20.sp)

                Spacer(Modifier.height(12.dp))
                Text(kata.label, color = tinta.warna, fontSize = 48.sp)

                Spacer(Modifier.height(64.dp))
                val pilihanKiri = if (inkleft) Choice.WORD_COLOUR else Choice.WORD
                val pilihanKanan = if (inkleft) Choice.WORD else Choice.WORD_COLOUR
                val jawabanBenar = if (mode == Mode.COLOR) Choice.WORD_COLOUR else Choice.WORD

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    listOf(pilihanKiri, pilihanKanan).forEach { pilihan ->
                        Button(onClick = { jawab(pilihan == jawabanBenar) }) {
                            Text(if (pilihan == Choice.WORD_COLOUR) tinta.label else kata.label)
                        }
                    }
                }
            }

            GameState.GAME_OVER -> {
                Text("Game Over!", fontSize = 28.sp)
                Spacer(Modifier.height(24.dp))
                Text("Your Score", fontSize = 16.sp)
                Text("$skor", fontSize = 24.sp)
                Spacer(Modifier.height(8.dp))
                Text("Best Score", fontSize = 12.sp)
                Text("$best", fontSize = 16.sp)
                Spacer(Modifier.height(24.dp))
                Button(onClick = { mulai() }) { Text("Restart Game") }
                Spacer(Modifier.height(8.dp))
                Button(onClick = { state = GameState.START }) { Text("Exit") }
            }
        }
    }
}
