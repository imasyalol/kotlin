package com.example.contacts.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.contacts.R
import com.example.contacts.ui.adapters.ContactAdapter
import com.example.contacts.data.Contact
import com.example.contacts.data.fetchAllContacts


class MainActivity : AppCompatActivity() {

    lateinit var rv : RecyclerView
    lateinit var contacts : List<Contact>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!checkPermissionContacts()) {
            requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), 1)
        }
        else
        {
            if (!checkPermissionSms()) {
                requestPermissions(arrayOf(Manifest.permission.SEND_SMS), 2)
            }
            setContentView(R.layout.activity_main)
            contacts = applicationContext.fetchAllContacts()
            initRecyclerView()
            val text = resources.getText(R.string.info)
            Toast.makeText(this, "${contacts.size} " + text, Toast.LENGTH_LONG).show()
        }
    }

    private fun initRecyclerView() {
        val llm = LinearLayoutManager(this)
        val adapter = ContactAdapter(contacts)
        llm.orientation = LinearLayoutManager.VERTICAL
        rv = findViewById(R.id.my_recycler_view)
        rv.layoutManager = llm
        rv.adapter = adapter
    }

    private fun checkPermissionContacts() : Boolean = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED


    private fun checkPermissionSms() : Boolean = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setContentView(R.layout.activity_main)
                    contacts = applicationContext.fetchAllContacts()
                    if (!checkPermissionSms()) {
                        requestPermissions(arrayOf(Manifest.permission.SEND_SMS), 2)
                    }
                    initRecyclerView()
                }
                else {
                    setContentView(R.layout.activity_main)
                    Toast.makeText(this, "Нет доступа к списку контактов", Toast.LENGTH_LONG).show()
                }

            }
            2 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                }
                else {
                    Toast.makeText(this, "Без данного доступа вы не сможете отпрвить сообщение", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

}