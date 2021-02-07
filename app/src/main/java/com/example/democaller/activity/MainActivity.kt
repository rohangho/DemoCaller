package com.example.democaller.activity

import android.Manifest
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.democaller.R
import com.example.democaller.adapter.PhoneDisplayerAdapter
import com.example.democaller.broadcastReceiver.PhoneStatReceiver
import com.example.democaller.model.DisplayModel


class MainActivity : AppCompatActivity() {

    var incomingCall = PhoneStatReceiver()
    private lateinit var phoneDisplayer: RecyclerView
    private var abc: ArrayList<DisplayModel> = ArrayList()
    private lateinit var phoneDisplayerAdapter: PhoneDisplayerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        phoneDisplayer = findViewById(R.id.numberDisplayer)
        phoneDisplayer.layoutManager = LinearLayoutManager(this)
        getPermission()


    }

    override fun onResume() {
        super.onResume()
        this.registerReceiver(incomingCall, IntentFilter())
        abc = ArrayList()
        getContactList()
    }

    private fun getPermission() {
        if ((ContextCompat.checkSelfPermission
                (this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) &&
            (ContextCompat.checkSelfPermission
                (this, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED)
            && (ContextCompat.checkSelfPermission
                (this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
            && (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CALL_LOG
            ) != PackageManager.PERMISSION_GRANTED)
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.WRITE_CONTACTS,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.READ_CALL_LOG
                ),
                100
            )

        } else {
            getContactList()
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
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
        val cur: Cursor? = cr.query(
            ContactsContract.Contacts.CONTENT_URI,
            null, null, null, null
        )
        if ((cur?.count ?: 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                val id: String = cur.getString(
                    cur.getColumnIndex(ContactsContract.Contacts._ID)
                )
                val name: String = cur.getString(
                    cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME
                    )
                )
                if (cur.getInt(
                        cur.getColumnIndex(
                            ContactsContract.Contacts.HAS_PHONE_NUMBER
                        )
                    ) > 0
                ) {
                    val pCur: Cursor? = cr.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        arrayOf(id),
                        null
                    )
                    while (pCur!!.moveToNext()) {
                        val phoneNo: String = pCur.getString(
                            pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER
                            )
                        )
                        val displayModel = DisplayModel(name, phoneNo)
                        abc.add(displayModel)
                    }
                    pCur.close()
                }
            }
        }
        cur?.close()
        phoneDisplayerAdapter = PhoneDisplayerAdapter(abc)
        phoneDisplayer.adapter = phoneDisplayerAdapter


    }

    fun openAddContact(view: View) {
        startActivity(Intent(MainActivity@ this, AddContact::class.java))
    }
}