package com.example.laundry.adapter
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.laundry.R
import com.example.laundry.activity_tambahan_pelanggan
import com.example.laundry.modeldata.modelpelanggan
import com.example.laundry.pelanggan.TambahPelanggan

class adapter_data_pelanggan(private val listPelanggan: ArrayList<modelpelanggan>) :
    RecyclerView.Adapter<adapter_data_pelanggan.ViewHolder>() {
    lateinit var appContext: Context
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_pelanggan2, parent, false)
        appContext = parent.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: adapter_data_pelanggan.ViewHolder, position: Int) {
        val item = listPelanggan[position]
        holder.id_pelanggan.text = item.id_pelanggan
        holder.nama.text = item.nama
        holder.alamat.text = item.alamat
        holder.nohp.text = item.nohp
        holder.terdaftar.text = item.terdaftar
        holder.bthubungi.setOnClickListener{

        }
        holder.btlihat.setOnClickListener{

        }

        holder.cvcardpelanggan.setOnClickListener {
            val intent = Intent(appContext, TambahPelanggan::class.java)
            intent.putExtra("judul", "Edit Pelanggan")
            intent.putExtra("id", item.id_pelanggan)
            intent.putExtra("nama", item.nama)
            intent.putExtra("alamat", item.alamat)
            intent.putExtra("nohp", item.nohp)
            appContext.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return listPelanggan.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cvcardpelanggan: CardView = itemView.findViewById(R.id.cv_card_pelanggan)
        val id_pelanggan: TextView = itemView.findViewById(R.id.id_pelanggan)
        val nama: TextView = itemView.findViewById(R.id.tv_nama)
        val alamat: TextView = itemView.findViewById(R.id.tv_alamat)
        val nohp: TextView = itemView.findViewById(R.id.tv_nohp)
        val terdaftar: TextView= itemView.findViewById(R.id.tv_terdaftar)
        val bthubungi: Button = itemView.findViewById(R.id.bt_hubungi)
        val btlihat: Button = itemView.findViewById(R.id.bt_lihat)
    }
}
