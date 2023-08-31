package com.app.amtadminapp

import android.app.Application
import android.content.Context

class App : Application() {
//    private var mFirebaseAnalytics: FirebaseAnalytics? = null

    override fun onCreate() {
        super.onCreate()
        application = this
//        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }

    companion object {
        var application: Context? = null
    }
}