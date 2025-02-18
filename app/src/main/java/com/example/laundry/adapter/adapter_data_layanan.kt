package com.example.laundry.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.laundry.R
import com.example.laundry.modeldata.modellayanan

class adapter_data_layanan (
    private val listLayanan: ArrayList<modellayanan>) :
        RecyclerView.Adapter<adapter_data_layanan.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_layanan, parent, false)
            return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: adapter_data_layanan.ViewHolder, position: Int) {
        val item = listLayanan[position]
        holder.id_layanan.text = item.id_layanan ?: ""
        holder.tv_namalayanan.text = item.tv_nama_layanan ?: ""
        holder.tv_harga.text = item.tv_harga ?: ""
        holder.tv_cabang.text = item.tv_cabang ?: ""
    }


    override  fun getItemCount(): Int {
        return listLayanan.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val id_layanan: TextView = itemView.findViewById(R.id.id_layanan)
        val tv_namalayanan: TextView = itemView.findViewById(R.id.tv_nama_layanan)
        val tv_harga: TextView = itemView.findViewById(R.id.tv_harga)
        val tv_cabang: TextView = itemView.findViewById(R.id.tv_cabang)

    }
}
