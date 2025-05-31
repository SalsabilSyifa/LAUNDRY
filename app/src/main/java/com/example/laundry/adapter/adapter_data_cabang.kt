package com.example.laundry.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.laundry.R
import com.example.laundry.activity_tambah_cabang
import com.example.laundry.adapter.adapter_data_pegawai.ViewHolder
import com.example.laundry.modeldata.modelcabang
import com.example.laundry.modeldata.modelpegawai
import com.example.laundry.tambah_pegawai

class adapter_data_cabang (private val listCabang: ArrayList<modelcabang>) :
    RecyclerView.Adapter<adapter_data_cabang.ViewHolder>() {
    lateinit var appContext: Context
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_cabang, parent, false)
        appContext = parent.context
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listCabang[position]
        holder.id_cabang.text = item.id_cabang
        holder.namacabang.text = item.tv_nama_cabang
        holder.alamatcabang.text = item.tv_alamat_cabang
        holder.layanancabang.text = item.tv_layanan_cabang
        holder.bthubungicabang.setOnClickListener{

        }
        holder.btlihatcabang.setOnClickListener{

        }

        holder.cvcabang.setOnClickListener {
            val intent = Intent(appContext, activity_tambah_cabang::class.java)
            intent.putExtra("judul", "Edit Cabang")
            intent.putExtra("id", item.id_cabang)
            intent.putExtra("tv_nama_cabang", item.tv_nama_cabang)
            intent.putExtra("tv_alamat_cabang", item.tv_alamat_cabang)
            intent.putExtra("tv_layanan_cabang", item.tv_layanan_cabang)
            appContext.startActivity(intent)

        }
    }

    override fun getItemCount(): Int {
        return listCabang.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cvcabang: CardView = itemView.findViewById(R.id.cv_cabang)
        val id_cabang: TextView = itemView.findViewById(R.id.id_cabang)
        val namacabang: TextView = itemView.findViewById(R.id.tv_nama_cabang)
        val alamatcabang: TextView = itemView.findViewById(R.id.tv_alamat_cabang)
        val layanancabang: TextView = itemView.findViewById(R.id.tv_layanan_cabang)
        val bthubungicabang: Button = itemView.findViewById(R.id.bt_hubungi_cabang)
        val btlihatcabang: Button = itemView.findViewById(R.id.bt_lihat_cabang)
    }
}

