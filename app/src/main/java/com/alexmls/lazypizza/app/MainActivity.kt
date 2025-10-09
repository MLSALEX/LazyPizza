package com.alexmls.lazypizza.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.alexmls.lazypizza.app.navigation.Navigation
import com.alexmls.lazypizza.core.designsystem.theme.LazyPizzaTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            LazyPizzaTheme {
                Navigation(navController = navController)
            }
        }
    }
}


