package com.adhanjadevelopers.girl_rescue.ui.activities

import android.app.Application
import com.todo.shakeit.core.ShakeIt

class Usalama :Application() {
    override fun onCreate() {
        super.onCreate()
        ShakeIt.init(this)
    }
}