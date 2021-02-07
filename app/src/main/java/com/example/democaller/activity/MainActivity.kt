package com.example.democaller.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.democaller.R
import com.example.democaller.adapter.PhoneDisplayerAdapter
import com.example.democaller.broadcastReceiver.PhoneStatReceiver
import com.example.democaller.model.DisplayModel
import com.example.democaller.utility.OnSelectItem


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
        phoneDisplayerAdapter = PhoneDisplayerAdapter(abc, object : OnSelectItem {
            override fun onSelected(position: Int) {
                showOptions(position)
            }

        })
        phoneDisplayer.adapter = phoneDisplayerAdapter


    }

    fun deleteContact(ctx: Context, phone: String?, name: String?): Boolean {
        val contactUri: Uri =
            Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phone))
        val cur: Cursor = ctx.contentResolver.query(contactUri, null, null, null, null)!!
        try {
            if (cur.moveToFirst()) {
                do {
                    if (cur.getString(cur.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME))
                            .equals(name)
                    ) {
                        val lookupKey: String =
                            cur.getString(cur.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY))
                        val uri: Uri = Uri.withAppendedPath(
                            ContactsContract.Contacts.CONTENT_LOOKUP_URI,
                            lookupKey
                        )
                        ctx.contentResolver.delete(uri, null, null)
                        return true
                    }
                } while (cur.moveToNext())
            }
        } catch (e: java.lang.Exception) {
            println(e.stackTrace)
        } finally {
            cur.close()
        }
        return false
    }

    private fun showOptions(position: Int) {
        val factory = LayoutInflater.from(this)
        val deleteDialogView = factory.inflate(R.layout.custom_chili_dialog, null)
        val deleteDialog = AlertDialog.Builder(this).create()
        deleteDialog.setView(deleteDialogView)
        deleteDialog.show()
    }


    fun openAddContact(view: View) {
        startActivity(Intent(MainActivity@ this, AddContact::class.java))
    }
}