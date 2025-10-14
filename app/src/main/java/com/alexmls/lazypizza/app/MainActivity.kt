package com.alexmls.lazypizza.app

import android.content.pm.ApplicationInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.alexmls.lazypizza.app.navigation.Navigation
import com.alexmls.lazypizza.catalog.data.seed.ProductSeeder
import com.alexmls.lazypizza.catalog.data.seed.seedProducts
import com.alexmls.lazypizza.catalog.data.seed.seedToppings
import com.alexmls.lazypizza.core.common.AnonymousAuthInitializer
import com.alexmls.lazypizza.core.designsystem.theme.LazyPizzaTheme
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val anonymousAuthInitializer: AnonymousAuthInitializer by inject()
    private val productSeeder: ProductSeeder by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        lifecycleScope.launch {
            try {
                anonymousAuthInitializer.ensureSignedIn()
            } catch (t: Throwable) {
                t.printStackTrace()
            }
        }
        val isDebug = 0 != (applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE)
        if (isDebug) {
            lifecycleScope.launch {
                try {
                    productSeeder.seedProductsOnce(seedProducts)
                    productSeeder.seedToppingsOnce(seedToppings)
                } catch (t: Throwable) {
                    t.printStackTrace()
                }
            }
        }
        enableEdgeToEdge()
        setContent {
            LazyPizzaTheme {
                val navController = rememberNavController()
                Navigation(navController = navController)
            }
        }
    }
}


