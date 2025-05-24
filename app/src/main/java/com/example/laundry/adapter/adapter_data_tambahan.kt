package com.example.laundry.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.laundry.R
import com.example.laundry.modeldata.modellayanan
import com.example.laundry.modeldata.modeltambahan

class adapter_data_tambahan (
    private val listTambahan: ArrayList<modeltambahan>) :
    RecyclerView.Adapter<adapter_data_tambahan.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_tambahan, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: adapter_data_tambahan.ViewHolder, position: Int) {
        val item = listTambahan[position]
        holder.id_tambahan.text = item.id_tambahan ?: ""
        holder.tv_tambahan.text = item.nama_tambahan ?: ""
        holder.tv_harga.text = item.harga_tambahan ?: ""
    }


    override  fun getItemCount(): Int {
        return listTambahan.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val id_tambahan: TextView = itemView.findViewById(R.id.id_tambahan)
        val tv_tambahan: TextView = itemView.findViewById(R.id.tv_tambahan)
        val tv_harga: TextView = itemView.findViewById(R.id.tv_harga)

    }
}
