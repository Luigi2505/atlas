package com.equipe.treinoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.equipe.treinoapp.ui.navigation.AppNavigation
import com.equipe.treinoapp.ui.theme.TreinoAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TreinoAppTheme {
                AppNavigation()
            }
        }
    }
}
