package com.darien.vpweek3

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

enum class Move(val emoji: String, val label: String) {
    ROCK("✊", "Rock"),
    PAPER("✋", "Paper"),
    SCISSOR("✌️", "Scissor")
}

enum class State {
    INITIAL, PICK, REVEAL, FINISHED
}

enum class Result(val teks: String) {
    WIN("You Win!"),
    LOSE("You Lose!"),
    DRAW("It's A Draw!")
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun Soal4Preview() {
    Soal4View()
}

@Composable
fun Soal4View(modifier: Modifier = Modifier) {
    val n = 5
    val target = (n / 2) + 1
    var state by rememberSaveable { mutableStateOf(State.INITIAL) }
    var skor by rememberSaveable { mutableIntStateOf(0) }
    var skorsistem by rememberSaveable { mutableIntStateOf(0) }
    var best by rememberSaveable { mutableIntStateOf(0) }
    var pilihanPemain by rememberSaveable { mutableStateOf(Move.ROCK) }
    var pilihansistem by rememberSaveable { mutableStateOf(Move.ROCK) }
    var hasil by rememberSaveable { mutableStateOf(Result.DRAW) }
    var urutan by remember { mutableStateOf(Move.entries.shuffled()) }

    LaunchedEffect(state) {
        if (state == State.REVEAL) {
            delay(700)
            if (skor == target || skorsistem == target) {
                if (skor - skorsistem > best) {
                    best = skor - skorsistem
                }
                state = State.FINISHED
            } else {
                urutan = Move.entries.shuffled()
                state = State.PICK
            }
        }
    }

    fun pilih(pilihan: Move) {
        pilihanPemain = pilihan
        pilihansistem = Move.entries.random()

        hasil = when {
            pilihanPemain == pilihansistem -> Result.DRAW
            (pilihanPemain == Move.ROCK && pilihansistem == Move.SCISSOR) ||
                    (pilihanPemain == Move.PAPER && pilihansistem == Move.ROCK) ||
                    (pilihanPemain == Move.SCISSOR && pilihansistem == Move.PAPER) -> Result.WIN

            else -> Result.LOSE
        }

        if (hasil == Result.WIN) {
            skor++
        }
        if (hasil == Result.LOSE) {
            skorsistem++
        }
        state = State.REVEAL
    }

    fun resetMatch(nextState: State) {
        skor = 0
        skorsistem = 0
        urutan = Move.entries.shuffled()
        state = nextState
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
            Text("🧑 $skor — $skorsistem ")
            Text("Best of $n")
        }

        when (state) {
            State.INITIAL -> {
                Text(
                    "Rock • Paper • Scissors",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(top = 48.dp, bottom = 24.dp)
                )
                Button({ state = State.PICK }) { Text("Start") }
            }

            State.PICK -> {
                Text("Pick your move!", modifier = Modifier.padding(top = 48.dp))
                Text("❔ VS ❔", fontSize = 36.sp, modifier = Modifier.padding(vertical = 16.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    urutan.forEach { r ->
                        Button({ pilih(r) }) { Text("${r.emoji} ${r.label}") }
                    }
                }
            }

            State.REVEAL -> {
                Text(
                    "${pilihanPemain.emoji} VS ${pilihansistem.emoji}",
                    fontSize = 36.sp,
                    modifier = Modifier.padding(top = 48.dp)
                )
                Text(hasil.teks, modifier = Modifier.padding(top = 8.dp))
            }

            State.FINISHED -> {
                Text(
                    if (skor > skorsistem) "You Win the Match!" else "You Lose the Match!",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(top = 48.dp)
                )
                Text(
                    "Best Score: $best",
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
                )
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Button({ resetMatch(State.PICK) }) { Text("Restart") }
                    Button({ resetMatch(State.INITIAL) }) { Text("Exit") }
                }
            }
        }
    }
}