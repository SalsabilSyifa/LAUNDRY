package com.example.laundry.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.laundry.R
import com.example.laundry.modeldata.modeltambahan

class adapter_transaksi_tambahan(
    private val list: MutableList<modeltambahan>,
    private val onDeleteClick: (modeltambahan) -> Unit
) : RecyclerView.Adapter<adapter_transaksi_tambahan.ViewHolder>() {
    lateinit var appContext: Context

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cv_tambahan2 : CardView = itemView.findViewById(R.id.cv_tambahan2)
        val tvNama: TextView = itemView.findViewById(R.id.tv_nama_tambahan2)
        val tvHarga: TextView = itemView.findViewById(R.id.tv_harga2)
        val tvNomor: TextView = itemView.findViewById(R.id.tv_tambahan_nomor)
        val btn_delete: ImageView = itemView.findViewById(R.id.btn_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_tambahan2, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.tvNomor.text = "[${position + 1}]"
        holder.tvNama.text = item.nama_tambahan ?: "-"
        holder.tvHarga.text = "Rp${item.harga_tambahan ?: "0"}"


        holder.btn_delete.setOnClickListener {
            list.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, list.size)
        }

    }

    override fun getItemCount(): Int = list.size
}