package com.adhanjadevelopers.girl_rescue.utils

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class SmsDeliveredBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        when (resultCode) {
            Activity.RESULT_OK -> {
               // Timber.d("onReceive: sms delivered")
            }
            Activity.RESULT_CANCELED -> {
                //Timber.d("onReceive: sms not delivered")
            }
        }
    }
}