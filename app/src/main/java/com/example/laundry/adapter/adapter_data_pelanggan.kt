package com.example.laundry.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.laundry.R
import com.example.laundry.modeldata.modelpelanggan

class adapter_data_pelanggan(
    private val listPelanggan: ArrayList<modelpelanggan>) :
    RecyclerView.Adapter<adapter_data_pelanggan.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_pelanggan2, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: adapter_data_pelanggan.ViewHolder, position: Int) {
        val item = listPelanggan[position]
        holder.id_pelanggan.text = item.id_pelanggan ?: ""
        holder.tv_nama.text = item.tv_nama ?: ""
        holder.tv_alamat.text = item.tv_alamat ?: ""
        holder.tv_nohp.text = item.tv_nohp ?: ""
        holder.tv_terdaftar.text = item.tv_terdaftar ?: ""

        holder.bt_hubungi.setOnClickListener{

        }
        holder.bt_lihat.setOnClickListener{

        }
    }

    override fun getItemCount(): Int {
        return listPelanggan.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val id_pelanggan: TextView = itemView.findViewById(R.id.id_pelanggan)
        val tv_nama: TextView = itemView.findViewById(R.id.tv_nama)
        val tv_alamat: TextView = itemView.findViewById(R.id.tv_alamat)
        val tv_nohp: TextView = itemView.findViewById(R.id.tv_nohp)
        val tv_terdaftar: TextView= itemView.findViewById(R.id.tv_terdaftar)
        val bt_hubungi: Button = itemView.findViewById(R.id.bt_hubungi)
        val bt_lihat: Button = itemView.findViewById(R.id.bt_lihat)
    }
}
