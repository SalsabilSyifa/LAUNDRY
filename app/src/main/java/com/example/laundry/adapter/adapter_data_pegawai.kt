package com.example.laundry.adapter
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
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
        holder.bthubungipegawai.setOnClickListener {
            val context = holder.itemView.context
            val dialogView = LayoutInflater.from(context).inflate(R.layout.activity_dialog_hubungi, null)

            val dialog = AlertDialog.Builder(context)
                .setView(dialogView)
                .create()
            // Aksi WhatsApp
            dialogView.findViewById<Button>(R.id.buttonHubungiWA).setOnClickListener {
                val nomor = item.nohppegawai
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse("https://wa.me/$nomor")
                context.startActivity(intent)
            }

            // Aksi Telepon
            dialogView.findViewById<Button>(R.id.buttonHubungiTelepon).setOnClickListener {
                val nomor = item.nohppegawai
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:$nomor")
                context.startActivity(intent)
            }

            // Aksi Batal
            dialogView.findViewById<TextView>(R.id.buttonBatal).setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }

        holder.btlihatpegawai.setOnClickListener {
            val context = holder.itemView.context
            val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_mod_pegawai, null)

            // Mengisi data ke TextView
            dialogView.findViewById<TextView>(R.id.tv_dialog_mod_pegawai_id_isi).text = item.id_pegawai
            dialogView.findViewById<TextView>(R.id.tv_dialog_mod_pegawai_nama_isi).text = item.namapegawai
            dialogView.findViewById<TextView>(R.id.tv_dialog_mod_pelanggan_alamat_isi).text = item.alamatpegawai
            dialogView.findViewById<TextView>(R.id.tv_dialog_mod_pelanggan_nohp_isi).text = item.nohppegawai
            dialogView.findViewById<TextView>(R.id.tv_dialog_mod_pelanggan_terdaftar_isi).text = item.terdaftarpegawai
            dialogView.findViewById<TextView>(R.id.tv_dialog_mod_pelanggan_cabang_isi).text = item.cabangpegawai

            val dialog = AlertDialog.Builder(context)
                .setView(dialogView)
                .create()

            val btnEdit = dialogView.findViewById<Button>(R.id.bt_dialog_mod_pegawai_edit)
            val btnHapus = dialogView.findViewById<Button>(R.id.bt_dialog_mod_pegawai_hapus)

            btnEdit.setOnClickListener {
                val intent = Intent(context, tambah_pegawai::class.java)
                intent.putExtra("judul", "Edit Pegawai")
                intent.putExtra("id", item.id_pegawai)
                intent.putExtra("nama", item.namapegawai)
                intent.putExtra("alamat", item.alamatpegawai)
                intent.putExtra("nohp", item.nohppegawai)
                intent.putExtra("terdaftar", item.terdaftarpegawai)
                intent.putExtra("cabang", item.cabangpegawai)
                context.startActivity(intent)
            }

            btnHapus.setOnClickListener {
                dialog.dismiss()
                listener?.onDeleteClick(item)
            }

            dialog.show()
        }

    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onDeleteClick(pegawai: modelpegawai)
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
