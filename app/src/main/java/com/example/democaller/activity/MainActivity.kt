package com.example.democaller.activity

import android.Manifest
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.democaller.broadcastReceiver.PhoneStatReceiver
import com.example.democaller.R


class MainActivity : AppCompatActivity() {

    var incomingCall = PhoneStatReceiver()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getPermission()


    }

    override fun onResume() {
        super.onResume()
        this.registerReceiver(incomingCall, IntentFilter())
    }

    private fun getPermission() {
        if ((ContextCompat.checkSelfPermission
                (this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission
                (this, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED)
                && (ContextCompat.checkSelfPermission
                (this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
                && (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED)
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS,Manifest.permission.READ_PHONE_STATE,Manifest.permission.READ_CALL_LOG), 100)

        } else {
            getContactList()
        }
    }



    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            100 -> {

                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                    Log.i("Hii", "Permission has been denied by user")
                } else {
                    Log.i("Hii", "Permission has been granted by user")
                    getContactList()
                }
            }
        }
    }

    private fun getContactList() {
        val cr = contentResolver
        val cur: Cursor? = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null)
        if ((cur?.count ?: 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                val id: String = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID))
                val name: String = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME))
                if (cur.getInt(cur.getColumnIndex(
                                ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    val pCur: Cursor? = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", arrayOf(id), null)
                    while (pCur!!.moveToNext()) {
                        val phoneNo: String = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER))
                        Log.i("hii", "Name: $name")
                        Log.i("hii", "Phone Number: $phoneNo")
                    }
                    pCur.close()
                }
            }
        }
        cur?.close()
    }
}