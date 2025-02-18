package com.example.laundry.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.laundry.R
import com.example.laundry.modeldata.modelpegawai

class adapter_data_pegawai (
    private val listPegawai: ArrayList<modelpegawai>) :
    RecyclerView.Adapter<adapter_data_pegawai.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_pegawai, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: adapter_data_pegawai.ViewHolder, position: Int) {
        val item = listPegawai[position]
        holder.idpegawai.text = item.id_pegawai ?: ""
        holder.tvnamapegawai.text = item.tv_nama_pegawai ?: ""
        holder.tvalamatpegawai.text = item.tv_alamat_pegawai ?: ""
        holder.tvnohppegawai.text = item.tv_nohp_pegawai ?: ""
        holder.tvterdaftarpegawai.text = item.tv_terdaftar_pegawai ?: ""
        holder.tvcabangpegawai.text = item.tv_cabang_pegawai ?: ""

        holder.bthubungipegawai.setOnClickListener{

        }
        holder.btlihatpegawai.setOnClickListener{

        }
    }

    override fun getItemCount(): Int {
        return listPegawai.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val idpegawai: TextView = itemView.findViewById(R.id.id_pegawai)
        val tvnamapegawai: TextView = itemView.findViewById(R.id.tv_nama_pegawai)
        val tvalamatpegawai: TextView = itemView.findViewById(R.id.tv_alamat_pegawai)
        val tvnohppegawai: TextView = itemView.findViewById(R.id.tv_nohp_pegawai)
        val tvterdaftarpegawai: TextView = itemView.findViewById(R.id.tv_terdaftar_pegawai)
        val tvcabangpegawai: TextView = itemView.findViewById(R.id.tv_cabang_pegawai)
        val bthubungipegawai: Button = itemView.findViewById(R.id.bt_hubungi_pegawai)
        val btlihatpegawai: Button = itemView.findViewById(R.id.bt_lihat_pegawai)
    }
    }
