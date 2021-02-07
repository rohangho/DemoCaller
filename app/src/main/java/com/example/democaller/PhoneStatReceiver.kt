package com.example.democaller

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.TELECOM_SERVICE
import android.content.Intent
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.telephony.TelephonyManager.CALL_STATE_IDLE
import android.telephony.TelephonyManager.CALL_STATE_RINGING
import android.util.Log
import android.widget.Toast
import java.lang.reflect.Method
import kotlin.text.Typography.tm


class PhoneStatReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val telephoneManager: TelephonyManager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        telephoneManager.listen(
                MyPhoneState(
                        context,
                        (System.currentTimeMillis() / 1000).toString()
                ), PhoneStateListener.LISTEN_CALL_STATE
        )
    }

    class MyPhoneState(private val context: Context, private val timestamp: String) :
        PhoneStateListener() {
        override fun onCallStateChanged(state: Int, phoneNumber: String?) {

            if (state == CALL_STATE_RINGING) {
                Log.d("hii", phoneNumber!!)
            } else if (state == CALL_STATE_IDLE) {

            }
        }
    }


}