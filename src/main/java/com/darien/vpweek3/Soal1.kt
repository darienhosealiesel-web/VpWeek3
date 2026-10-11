package com.darien.vpweek3

import androidx.compose.foundation.Image
import com.darien.vpweek3.R
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.random.Random

enum class halaman(
    val topText: String,
    val middleText: String,
    val bottomText: String,
    val wallpaper: Color,
    val icon: Int
) {
    FIRST("Reaction", "Test", "Click to Start", Color(0xFF71CEDD), R.drawable.baseline_bolt_24),
    WAIT(
        "Get Ready",
        "Wait for green light...",
        "DON'T CLICK YET!",
        Color(0xFFD9D9D9),
        R.drawable.warning_24dp_e3e3e3_fill0_wght400_grad0_opsz24
    ),
    STARTTRIAL(
        "GO!",
        "CLICK NOW!",
        "TAP AS FAST AS YOU CAN!",
        Color(0xFF4CAF50),
        R.drawable.baseline_directions_walk_24
    ),
    FAIL(
        "FAIL!",
        "You clicked too early, TRY TO READ THE RULE BRO",
        "TRY AGAIN",
        Color(0xFFFA4444),
        R.drawable.thumb_down_24dp_e3e3e3_fill0_wght400_grad0_opsz24
    ),
    DONE("", "", "", Color(0xFF4CAF50), R.drawable.baseline_check_circle_24),
    FINAL("", "", "", Color.Gray, R.drawable.baseline_bolt_24)
}

enum class Category(val message: String, val background: Color, val photo: Int) {
    FAST("DA** YOU ARE SO FAST BRO!", Color(0xFF00E676), R.drawable.hormat),
    GOOD("YOUR REFLEX GOOD LA", Color(0xFF2196F3), R.drawable.jempol),
    NORMAL("MEH LIKE OTHER PERSON", Color(0xFFFF9800), R.drawable.biasaaja),
    BAD("YOU LIKE A SNAIL BRO", Color(0xFFFF5722), R.drawable.jempolbawah)
}

@Composable
fun Soal1View() {
    var pageState by remember { mutableStateOf(halaman.FIRST) }
    var start by remember { mutableStateOf(0L) }
    val results = remember { mutableStateListOf<Int?>() }
    LaunchedEffect(pageState) {
        if (pageState == halaman.WAIT) {
            delay(Random.nextLong(500, 4501))
            start = System.currentTimeMillis()
            pageState = halaman.STARTTRIAL
        }
    }

    val isValid = results.filterNotNull()
    var average = 9999
    if (isValid.isNotEmpty()) {
        average = isValid.average().toInt()
    }
    var averageText = ""
    if (isValid.isNotEmpty()) {
        averageText = "${average}ms"
    } else {
        averageText = "-"
    }
    val category = when {
        average < 180 -> Category.FAST
        average < 280 -> Category.GOOD
        average < 450 -> Category.NORMAL
        else -> Category.BAD
    }

    var background = pageState.wallpaper
    if (pageState == halaman.FINAL) {
        background = category.background
    }

    val top = when (pageState) {
        halaman.DONE -> "Trial ${results.size} Complete!"
        halaman.FINAL -> category.message
        else -> pageState.topText
    }
    val middle = when (pageState) {
        halaman.DONE -> "Time: ${results.last()}ms"
        halaman.FINAL -> "Average: $averageText"
        else -> pageState.middleText
    }
    val bottom = when (pageState) {
        halaman.DONE -> "Continue to Trial ${results.size + 1}"
        halaman.FINAL -> "Click to Start New Test"
        else -> pageState.bottomText
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(background)
            .clickable {
                when (pageState) {
                    halaman.FIRST -> pageState = halaman.WAIT
                    halaman.WAIT -> {
                        if (results.isNotEmpty()) {
                            results.add(null)
                        }
                        pageState = halaman.FAIL
                    }

                    halaman.STARTTRIAL -> {
                        results.add((System.currentTimeMillis() - start).toInt())
                        if (results.size == 3) {
                            pageState = halaman.FINAL
                        } else {
                            pageState = halaman.DONE
                        }
                    }

                    halaman.DONE -> pageState = halaman.WAIT
                    halaman.FAIL -> {
                        if (results.size == 3) {
                            pageState = halaman.FINAL
                        } else if (results.size == 2) {
                            pageState = halaman.WAIT
                        } else {
                            pageState = halaman.FIRST
                        }
                    }

                    halaman.FINAL -> {
                        results.clear()
                        pageState = halaman.FIRST
                    }
                }
            }) {
        Text(top, color = Color.White, fontSize = 30.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(30.dp))
        if (pageState == halaman.FINAL) {
            Image(
                painter = painterResource(category.photo),
                contentDescription = null,
                modifier = Modifier.size(120.dp)
            )
        } else {
            Icon(
                painter = painterResource(pageState.icon),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(120.dp)
            )
        }
        Spacer(Modifier.height(30.dp))
        Text(
            middle,
            color = Color.White,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(8.dp))
        Text(bottom, color = Color.White, fontSize = 15.sp)
        Spacer(Modifier.height(24.dp))

        if (results.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .background(Color.White, RoundedCornerShape(12.dp))
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Trial Results", color = Color(0xFF1976D2), fontWeight = FontWeight.Bold)

                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("1", color = Color(0xFF4CAF50))
                        Text(
                            if (results.size > 0 && results[0] != null) "${results[0]}ms" else "-",
                            color = Color.Black
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("2", color = Color(0xFF4CAF50))
                        Text(
                            if (results.size > 1 && results[1] != null) "${results[1]}ms" else "-",
                            color = Color.Black
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("3", color = Color(0xFF4CAF50))
                        Text(
                            if (results.size > 2 && results[2] != null) "${results[2]}ms" else "-",
                            color = Color.Black
                        )
                    }
                }
            }

            if (pageState == halaman.FINAL) {
                Spacer(Modifier.height(8.dp))
                Text("Average Score", color = Color(0xFF1976D2))
                Text(averageText, color = Color(0xFFE65100), fontWeight = FontWeight.Bold)
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun Soal1Preview() {
    Soal1View()
}