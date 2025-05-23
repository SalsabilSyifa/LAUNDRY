package com.example.laundry.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.laundry.R
import com.example.laundry.modeldata.modelTransaksiTambahan
import com.example.laundry.tambah_pegawai

class adapter_transaksi_tambahan(private val list: List<modelTransaksiTambahan>) :

    RecyclerView.Adapter<adapter_transaksi_tambahan.ViewHolder>() {
    lateinit var appContext: Context

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cv_tambahan2 : CardView = itemView.findViewById(R.id.cv_tambahan2)
        val tvNama: TextView = itemView.findViewById(R.id.tv_nama_tambahan2)
        val tvHarga: TextView = itemView.findViewById(R.id.tv_harga2)
        val tvNomor: TextView = itemView.findViewById(R.id.tv_tambahan_nomor)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_tambahan2, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.tvNomor.text = "[${position + 1}]"
        holder.tvNama.text = item.nama
        holder.tvHarga.text = "Rp${item.harga}"

        holder.cv_tambahan2.setOnClickListener {
            val intent = Intent(appContext, tambah_pegawai::class.java)
            intent.putExtra("nama_tambahan", item.nama)
            intent.putExtra("harga_tambahan", item.harga)
            appContext.startActivity(intent)

        }
    }

    override fun getItemCount(): Int = list.size
}
