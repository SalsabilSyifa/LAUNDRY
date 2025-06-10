package com.example.laundry.adapter
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.laundry.R
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
        holder.bthubungi.setOnClickListener {
            val context = holder.itemView.context
            val dialogView = LayoutInflater.from(context).inflate(R.layout.activity_dialog_hubungi, null)

            val dialog = AlertDialog.Builder(context)
                .setView(dialogView)
                .create()

            // Aksi untuk tombol WhatsApp
            dialogView.findViewById<Button>(R.id.buttonHubungiWA).setOnClickListener {
                val nomor = item.nohp
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse("https://wa.me/$nomor")
                context.startActivity(intent)
            }

            // Aksi untuk tombol Telepon
            dialogView.findViewById<Button>(R.id.buttonHubungiTelepon).setOnClickListener {
                val nomor = item.nohp
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:$nomor")
                context.startActivity(intent)
            }

            // Aksi untuk tombol Batal
            dialogView.findViewById<TextView>(R.id.buttonBatal).setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }

        holder.btlihat.setOnClickListener {
            val context = holder.itemView.context
            val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_mod_pelanggan, null)

            dialogView.findViewById<TextView>(R.id.tv_dialog_mod_pelanggan_id_isi).text = item.id_pelanggan
            dialogView.findViewById<TextView>(R.id.tv_dialog_mod_pelanggan_nama_isi).text = item.nama
            dialogView.findViewById<TextView>(R.id.tv_dialog_mod_pelanggan_alamat_isi).text = item.alamat
            dialogView.findViewById<TextView>(R.id.tv_dialog_mod_pelanggan_nohp_isi).text = item.nohp
            dialogView.findViewById<TextView>(R.id.tv_dialog_mod_pelanggan_terdaftar_isi).text = item.terdaftar

            val dialog = AlertDialog.Builder(context)
                .setView(dialogView)
                .create()

            val btEdit = dialogView.findViewById<Button>(R.id.bt_dialog_mod_pelanggan_edit)
            val btHapus = dialogView.findViewById<Button>(R.id.bt_dialog_mod_pelanggan_hapus)

            btEdit.setOnClickListener {
                val intent = Intent(context, TambahPelanggan::class.java)
                intent.putExtra("judul", "Edit Pelanggan")
                intent.putExtra("id", item.id_pelanggan)
                intent.putExtra("nama", item.nama)
                intent.putExtra("alamat", item.alamat)
                intent.putExtra("nohp", item.nohp)
                context.startActivity(intent)
                dialog.dismiss()
            }

            btHapus.setOnClickListener {
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
        fun onDeleteClick(pelanggan: modelpelanggan)
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
