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
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.laundry.R
import com.example.laundry.modeldata.modeltambahan

class adapter_pilihtambahan(
    private val context: Context,
    private var tambahanList: ArrayList<modeltambahan>
) : RecyclerView.Adapter<adapter_pilihtambahan.ViewHolder>(), Filterable {

    private var tambahanListFull: ArrayList<modeltambahan> = ArrayList(tambahanList)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_pilihtambahan, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = tambahanList[position]

        holder.tvID.text = "[${position + 1}]"
        holder.tvnamatambahan.text = currentItem.nama_tambahan ?: "-"
        holder.tvharga.text = "Rp${currentItem.harga_tambahan ?: "0"}"

        holder.itemView.setOnClickListener {
            val intent = Intent()
            intent.putExtra("nama_tambahan", currentItem.nama_tambahan)
            intent.putExtra("harga_tambahan", currentItem.harga_tambahan)
            intent.putExtra("deskripsi_tambahan", currentItem.deskripsi_tambahan)

            (context as Activity).setResult(Activity.RESULT_OK, intent)
            context.finish()
        }
    }

    override fun getItemCount(): Int = tambahanList.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList = ArrayList<modeltambahan>()
                if (constraint.isNullOrEmpty()) {
                    filteredList.addAll(tambahanListFull)
                } else {
                    val filterPattern = constraint.toString().lowercase().trim()
                    for (item in tambahanListFull) {
                        if ((item.nama_tambahan?.lowercase() ?: "").contains(filterPattern)) {
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
                tambahanList.clear()
                tambahanList.addAll(results?.values as ArrayList<modeltambahan>)
                notifyDataSetChanged()
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cvtambahan: CardView = itemView.findViewById(R.id.cv_pilihTambahan)
        val tvID: TextView = itemView.findViewById(R.id.tv_tambahan_nomor)
        val tvnamatambahan: TextView = itemView.findViewById(R.id.tv_nama_tambahan)
        val tvharga: TextView = itemView.findViewById(R.id.tv_harga)
    }
}
