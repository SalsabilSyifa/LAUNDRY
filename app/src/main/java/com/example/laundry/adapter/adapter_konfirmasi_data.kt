package com.example.laundry.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.laundry.R
import com.example.laundry.modeldata.modeltambahan

class adapter_konfirmasi_data (private val tambahanList: MutableList<modeltambahan>) :
    RecyclerView.Adapter<adapter_konfirmasi_data.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textNama: TextView = view.findViewById(R.id.tv_NamaTambahan)
        val textHarga: TextView = view.findViewById(R.id.tv_HargaTambahan)
        val textIndex: TextView = view.findViewById(R.id.textIndexTambahan)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cv_item_konfirmasi_tambahan, parent, false)
        return ViewHolder(view)


    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tambahan = tambahanList[position]
        holder.textIndex.text = "[${position + 1}]"
        holder.textNama.text = tambahan.nama_tambahan ?: "-"
        holder.textHarga.text = "Rp ${tambahan.harga_tambahan  ?: "0"}"
    }

    override fun getItemCount(): Int = tambahanList.size

}
