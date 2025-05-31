package com.example.laundry.adapter

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.laundry.Activity_transaksi
import com.example.laundry.R
import com.example.laundry.adapter.adapter_pilihpelanggan.ViewHolder
import com.example.laundry.modeldata.modellayanan
import com.example.laundry.modeldata.modelpelanggan

class adapter_pilihlayanan (private val layananList: ArrayList<modellayanan>) :
    RecyclerView.Adapter<adapter_pilihlayanan.ViewHolder>(), Filterable {
    lateinit var appContext: Context
    private var layananListFull: ArrayList<modellayanan> = ArrayList(layananList)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_pilihlayanan, parent, false)
        appContext = parent.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val nomor = position + 1
        val item = layananList[position]
        holder.tvID.text = "[$nomor]"
        holder.tvnamalayanan.text = item.tv_nama_layanan
        holder.tvharga.text = " ${item.tv_harga}"
        holder.cvcardlayanan.setOnClickListener {
            val intent = Intent(appContext, Activity_transaksi::class.java)
            intent.putExtra("id_layanan", item.id_layanan)
            intent.putExtra("tv_nama_layanan", item.tv_nama_layanan)
            intent.putExtra("tv_harga", item.tv_harga)
            (appContext as Activity).setResult(Activity.RESULT_OK, intent)
            (appContext as Activity).finish()
        }

    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList = ArrayList<modellayanan>()
                if (constraint == null || constraint.isEmpty()) {
                    filteredList.addAll(layananListFull)
                } else {
                    val filterPattern = constraint.toString().lowercase().trim()
                    for (item in layananListFull) {
                        if ((item.tv_nama_layanan?.lowercase() ?: "").contains(filterPattern)) {

                            filteredList.add(item)
                        }
                    }
                }
                val results = FilterResults()
                results.values = filteredList
                return results
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                layananList.clear()
                layananList.addAll(results?.values as List<modellayanan>)
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int = layananList.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cvcardlayanan: CardView = itemView.findViewById(R.id.cv_card_pilihlayanan)
        val tvID: TextView = itemView.findViewById(R.id.tv_layanan_nomor)
        val tvnamalayanan: TextView = itemView.findViewById(R.id.tv_nama_layanan)
        val tvharga: TextView = itemView.findViewById(R.id.tv_harga)
    }
}