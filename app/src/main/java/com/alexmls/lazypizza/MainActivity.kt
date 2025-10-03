package com.alexmls.lazypizza

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import com.alexmls.lazypizza.designsystem.theme.LazyPizzaTheme
import androidx.compose.ui.Modifier
import com.alexmls.lazypizza.designsystem.Adaptive
import com.alexmls.lazypizza.designsystem.rememberLayoutType


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LazyPizzaTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PizzaApp(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun PizzaApp(modifier: Modifier = Modifier) {
    val layout = rememberLayoutType()

    Adaptive(
        layout = layout,
        mobile = {  },
        wide   = {  }
    )
}

