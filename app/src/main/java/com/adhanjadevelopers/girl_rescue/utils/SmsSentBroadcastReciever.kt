package com.adhanjadevelopers.girl_rescue.utils

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsManager
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SmsSentBroadcastReciever() : BroadcastReceiver() {
    var smssCount = MutableLiveData<Int>(0)
    override fun onReceive(context: Context?, intent: Intent?) {
        when (resultCode) {
            Activity.RESULT_OK -> {
                CoroutineScope(Dispatchers.Main).launch {
                    smssCount.value = smssCount.value!!.plus(1)
                   // Timber.d("BroadcastReceiver SMS sent: ${smssCount.value}")
                }
            }
            SmsManager.RESULT_ERROR_GENERIC_FAILURE -> {
                //Timber.d("onReceive: generic failure")
            }
            SmsManager.RESULT_ERROR_NO_SERVICE -> {
                //Timber.d("onReceive:  no service")
            }
            SmsManager.RESULT_ERROR_NULL_PDU -> {
                //Timber.d("onReceive:  null pdu")
            }
            SmsManager.RESULT_ERROR_RADIO_OFF -> {
               // Timber.d("onReceive:  radio off")
            }
        }
    }
}