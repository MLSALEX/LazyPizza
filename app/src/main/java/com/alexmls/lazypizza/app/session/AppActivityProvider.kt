package com.alexmls.lazypizza.app.session

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.alexmls.lazypizza.core.common.ActivityProvider
import java.lang.ref.WeakReference

/**
 * Tracks current Activity via Application.ActivityLifecycleCallbacks.
 */
class AppActivityProvider :
    ActivityProvider,
    Application.ActivityLifecycleCallbacks {

    private var currentActivityRef: WeakReference<Activity?> = WeakReference(null)

    override val currentActivity: Activity?
        get() = currentActivityRef.get()

    override fun onActivityResumed(activity: Activity) {
        // Activity is visible → remember it
        currentActivityRef = WeakReference(activity)
    }

    override fun onActivityPaused(activity: Activity) {
        // Optional: clear when this activity goes to background
        if (currentActivityRef.get() === activity) {
            currentActivityRef = WeakReference(null)
        }
    }

    // Other callbacks not needed now
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) = Unit
    override fun onActivityStarted(activity: Activity) = Unit
    override fun onActivityStopped(activity: Activity) = Unit
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit
    override fun onActivityDestroyed(activity: Activity) = Unit
}