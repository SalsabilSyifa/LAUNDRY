package com.example.laundry.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.laundry.Activity_transaksi
import com.example.laundry.R
import com.example.laundry.modeldata.modelpelanggan
import com.google.firebase.database.DatabaseReference


class adapter_pilihpelanggan (private val pelangganList: ArrayList<modelpelanggan>) :
    RecyclerView.Adapter<adapter_pilihpelanggan.ViewHolder>(), Filterable {
    lateinit var appContext: Context
    private var pelangganListFull: ArrayList<modelpelanggan> = ArrayList(pelangganList)
    lateinit var databaseReference: DatabaseReference

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_pilihpelanggan, parent, false)
        appContext = parent.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val nomor = position + 1
        val item = pelangganList[position]
        holder.tvID.text = "[$nomor]"
        holder.tvnama.text = item.nama
        holder.tvalamat.text = " ${item.alamat}"
        holder.tvnohp.text = " ${item.nohp}"
        holder.cvcardpelanggan.setOnClickListener {
            val intent = Intent(appContext, Activity_transaksi::class.java)
            intent.putExtra("idpelanggan", item.id_pelanggan)
            intent.putExtra("nama", item.nama)
            intent.putExtra("nohp", item.nohp)
            (appContext as Activity).setResult(Activity.RESULT_OK, intent)
            (appContext as Activity).finish()
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList = ArrayList<modelpelanggan>()
                if (constraint == null || constraint.isEmpty()) {
                    filteredList.addAll(pelangganListFull)
                } else {
                    val filterPattern = constraint.toString().lowercase().trim()
                    for (item in pelangganListFull) {
                        if ((item.nama?.lowercase() ?: "").contains(filterPattern)) {

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
                pelangganList.clear()
                pelangganList.addAll(results?.values as List<modelpelanggan>)
                notifyDataSetChanged()
            }
        }
    }


    override fun getItemCount(): Int = pelangganList.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cvcardpelanggan: CardView = itemView.findViewById(R.id.cv_pilihPelanggan)
        val tvID: TextView = itemView.findViewById(R.id.tv_pelanggan_nomor)
        val tvnama: TextView = itemView.findViewById(R.id.tv_nama_pelanggan)
        val tvalamat: TextView = itemView.findViewById(R.id.tv_alamat)
        val tvnohp: TextView = itemView.findViewById(R.id.tv_nohp)
    }
}