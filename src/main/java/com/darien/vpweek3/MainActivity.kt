package com.darien.vpweek3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.darien.vpweek3.Soal2Bonus.Soal1Bonus
import com.darien.vpweek3.soal3.Soal3View


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Soal1Bonus()
        }
    }
}