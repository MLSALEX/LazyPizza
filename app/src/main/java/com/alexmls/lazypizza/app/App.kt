package com.alexmls.lazypizza.app

import android.app.Application
import com.alexmls.lazypizza.app.di.appModule
import com.alexmls.lazypizza.app.session.AppActivityProvider
import com.alexmls.lazypizza.authorization.di.authModule
import com.alexmls.lazypizza.cart_checkout.di.cartModule
import com.alexmls.lazypizza.catalog.di.catalogModule
import com.alexmls.lazypizza.history.di.historyModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {
    val applicationScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    lateinit var activityProvider: AppActivityProvider
        private set

    override fun onCreate() {
        super.onCreate()

        activityProvider = AppActivityProvider()
        registerActivityLifecycleCallbacks(activityProvider)

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(
                appModule,
                catalogModule,
                cartModule,
                historyModule,
                authModule
            )
        }
    }
}