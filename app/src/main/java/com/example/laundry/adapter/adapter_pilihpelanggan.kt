package com.example.laundry.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.laundry.R
import com.example.laundry.adapter.adapter_data_pelanggan.ViewHolder
import com.example.laundry.modeldata.modelpelanggan
import com.google.firebase.database.DatabaseReference


class adapter_pilihpelanggan (private val pelangganList: ArrayList<modelpelanggan>) :
    RecyclerView.Adapter<adapter_pilihpelanggan.ViewHolder>() {
    lateinit var appContext: Context
    lateinit var databaseReference: DatabaseReference
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_pilihpelanggan, parent, false)
        appContext = parent.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val nomor = position = 1
        val item = pelangganList[position]
        holder.id_pelanggan.text = "[{nomor}]"
        holder.tv_nama.text = item.nama
        holder.tv_alamat.text = "Alamat: {item.alamat}"
        holder.tv_nohp.text = "NoHP: {item.noHP}"
        holder.cvcardpelanggan.setOnClickListener{
            val intent = Intent(appContext. activity_transaksi:: class.java)
            intent.putExtra("idpelanggan", item.id_pelanggan)
            intent.putExtra("nama", item.nama)
            intent.putExtra("nohp", item.nohp)
            (appContext as Activity).setResult(Activity.RESULT_OK, intent)
            (appContext as Activity).finish()
        }

    }
}