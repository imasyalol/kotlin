package com.example.contacts.ui.adapters

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.telephony.SmsManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.contacts.R
import com.example.contacts.data.Contact


class ContactAdapter(var items: List<Contact>) : RecyclerView.Adapter<ContactAdapter.ContactHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            = ContactHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.contact_item,
            parent,
            false
        )
    )

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ContactHolder, position: Int) {
        if (ContextCompat.checkSelfPermission(holder.itemView.context, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            holder.itemView.findViewById<ImageButton>(R.id.msg_button).setOnClickListener {
                val smsManager: SmsManager = SmsManager.getDefault()
                smsManager.sendTextMessage(
                    items[position].phoneNumber,
                    null,
                    "Приветик, ${items[position].name}!!!",
                    null,
                    null
                )
                Toast.makeText(it.context, "Сообщение отправлено", Toast.LENGTH_LONG).show()
            }
        }

        holder.bind(items[position])
    }

    inner class ContactHolder(itemView: View) : RecyclerView.ViewHolder(itemView)  {

        private val phone = itemView.findViewById<TextView>(R.id.phone)
        private val name = itemView.findViewById<TextView>(R.id.name)
        private val photo = itemView.findViewById<ImageView>(R.id.photo)

        fun bind(item: Contact) {
            phone.text = item.phoneNumber
            name.text = item.name
            photo.setImageResource(R.mipmap.ic_launcher_round)
        }

    }

}