package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.ui.components.GlobalNeonRippleOverlay
import com.example.ui.navigation.SKMissionBoardNavGraph
import com.example.ui.theme.SKMissionBoardTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SKMissionBoardTheme {
                GlobalNeonRippleOverlay {
                    SKMissionBoardNavGraph()
                }
            }
        }
    }
}
