package com.example.laundry.adapter
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.laundry.R
import com.example.laundry.modeldata.modelpegawai
import com.example.laundry.tambah_pegawai

class adapter_data_pegawai (private val listPegawai: ArrayList<modelpegawai>) :
    RecyclerView.Adapter<adapter_data_pegawai.ViewHolder>() {
    lateinit var appContext: Context
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_pegawai, parent, false)
        appContext = parent.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listPegawai[position]
        holder.id_pegawai.text = item.id_pegawai
        holder.namapegawai.text = item.namapegawai
        holder.alamatpegawai.text = item.alamatpegawai
        holder.nohppegawai.text = item.nohppegawai
        holder.terdaftarpegawai.text = item.terdaftarpegawai
        holder.cabangpegawai.text = item.cabangpegawai
        holder.bthubungipegawai.setOnClickListener{

        }
        holder.btlihatpegawai.setOnClickListener{

        }

        holder.cvcardpegawai.setOnClickListener {
            val intent = Intent(appContext, tambah_pegawai::class.java)
            intent.putExtra("judul", "Edit Pegawai")
            intent.putExtra("id", item.id_pegawai)
            intent.putExtra("namapegawai", item.namapegawai)
            intent.putExtra("nohppegawai", item.nohppegawai)
            intent.putExtra("alamatpegawai", item.alamatpegawai)
            intent.putExtra("cabangpegawai", item.cabangpegawai)
            appContext.startActivity(intent)

        }
    }

    override fun getItemCount(): Int {
        return listPegawai.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cvcardpegawai: CardView = itemView.findViewById(R.id.cv_card_pegawai)
        val id_pegawai: TextView = itemView.findViewById(R.id.id_pegawai)
        val namapegawai: TextView = itemView.findViewById(R.id.tv_nama_pegawai)
        val alamatpegawai: TextView = itemView.findViewById(R.id.tv_alamat_pegawai)
        val nohppegawai: TextView = itemView.findViewById(R.id.tv_nohp_pegawai)
        val terdaftarpegawai: TextView = itemView.findViewById(R.id.tv_terdaftar_pegawai)
        val cabangpegawai: TextView = itemView.findViewById(R.id.tv_cabang_pegawai)
        val bthubungipegawai: Button = itemView.findViewById(R.id.bt_hubungi_pegawai)
        val btlihatpegawai: Button = itemView.findViewById(R.id.bt_lihat_pegawai)
    }
    }
