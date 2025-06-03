package com.example.laundry.adapter

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.icu.text.NumberFormat
import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.laundry.R
import com.example.laundry.konfirmasi_data
import com.example.laundry.modeldata.modelLaporan
import com.google.firebase.database.FirebaseDatabase
import java.util.Date
import java.util.Locale

class adapter_laporan(
    private val context: Context,
    private val listLaporan: List<modelLaporan>,
) : RecyclerView.Adapter<adapter_laporan.LaporanViewHolder>() {



    private fun formatRupiah(number: Int): String {
        val localeID = Locale("in", "ID")
        val format = NumberFormat.getCurrencyInstance(localeID)
        return format.format(number)
    }


    class LaporanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNoUrut: TextView = itemView.findViewById(R.id.tvAngka)
        val tvNama: TextView = itemView.findViewById(R.id.tvNama)
        val tvLayanan: TextView = itemView.findViewById(R.id.tvLayanan)
        val tvTotal: TextView = itemView.findViewById(R.id.tvTotal)
        val tvTanggal: TextView = itemView.findViewById(R.id.tvTanggal)
        val tvLayananTambahan: TextView = itemView.findViewById(R.id.tvTambahan)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        val btnAksi: Button = itemView.findViewById(R.id.btnAksi)
        val tvDiambil: TextView = itemView.findViewById(R.id.tvDiambil)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LaporanViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_laporan, parent, false)
        return LaporanViewHolder(view)
    }

    override fun onBindViewHolder(holder: LaporanViewHolder, position: Int) {
        val laporan = listLaporan[position]
        holder.tvNoUrut.text = "[${itemCount - position}]"
        holder.tvNama.text = laporan.namaPelanggan
        holder.tvTanggal.text = laporan.tanggal
        holder.tvLayanan.text = laporan.layananUtama


        val tambahanText = if (laporan.tambahan.isNotEmpty())
            laporan.tambahan.joinToString(", ")
        else
            "Tidak ada tambahan"
        holder.tvLayananTambahan.text = tambahanText

        val totalBayarInt = laporan.totalBayar.toIntOrNull() ?: 0
        holder.tvTotal.text = formatRupiah(totalBayarInt)



        holder.btnAksi.setOnClickListener {
            showMetodePembayaran(context, laporan.idTransaksi, position)
        }

        holder.tvDiambil.visibility = View.GONE
        holder.btnAksi.visibility = View.VISIBLE

        when (laporan.status) {
            "Belum Dibayar" -> {
                holder.tvStatus.text = "Belum Dibayar"
                holder.tvStatus.setBackgroundColor(context.getColor(R.color.orange))
                holder.tvStatus.setTextColor(context.getColor(R.color.white))

                holder.btnAksi.visibility = View.VISIBLE
                holder.btnAksi.text = "Bayar Sekarang"
                holder.btnAksi.setBackgroundColor(ContextCompat.getColor(context, R.color.red))
                holder.btnAksi.setOnClickListener {
                    showMetodePembayaran(context, laporan.idTransaksi, position)
                }

                holder.tvDiambil.visibility = View.GONE
            }

            "Lunas" -> {
                holder.tvStatus.text = "Sudah Dibayar"
                holder.tvStatus.setBackgroundColor(context.getColor(R.color.blue))
                holder.tvStatus.setTextColor(context.getColor(R.color.white))

                holder.btnAksi.visibility = View.VISIBLE
                holder.btnAksi.text = "Ambil Sekarang"
                holder.btnAksi.setBackgroundColor(ContextCompat.getColor(context, R.color.blue))
                holder.btnAksi.setOnClickListener {
                    val waktuAmbil = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale("in", "ID")).format(Date())
                    val ref = FirebaseDatabase.getInstance().getReference("laporan").child(laporan.idTransaksi)

                    ref.updateChildren(
                        mapOf(
                            "status" to "Selesai",
                            "diambilPada" to waktuAmbil
                        )
                    ).addOnSuccessListener {
                        laporan.status = "Selesai"
                        laporan.diambilPada = waktuAmbil
                        notifyItemChanged(position)
                    }
                }

                holder.tvDiambil.visibility = View.GONE
            }

            "Selesai" -> {
                holder.tvStatus.text = "Selesai"
                holder.tvStatus.setBackgroundColor(context.getColor(R.color.green_soft))
                holder.tvStatus.setTextColor(context.getColor(R.color.white))

                holder.btnAksi.visibility = View.GONE
                holder.tvDiambil.visibility = View.VISIBLE
                holder.tvDiambil.text = "Diambil Pada\n${laporan.diambilPada ?: "-"}"
            }

            else -> {
                // Fallback kalau status aneh
                holder.tvStatus.text = laporan.status
                holder.btnAksi.visibility = View.GONE
                holder.tvDiambil.visibility = View.GONE
            }
        }


    }

    override fun getItemCount(): Int = listLaporan.size

    private fun showMetodePembayaran(context: Context, transaksiId: String, position: Int) {
        val dialogView = LayoutInflater.from(context)
            .inflate(R.layout.activity_dialog_metode_pembayaran_laporan, null)

        val dialog = AlertDialog.Builder(context).setView(dialogView).create()

        val onClick: (String) -> Unit = { metode ->
            updateStatusPembayaran(transaksiId, metode, position)
            dialog.dismiss()
        }

        dialogView.findViewById<Button>(R.id.btnTunai).setOnClickListener { onClick("Tunai") }
        dialogView.findViewById<Button>(R.id.btnQRIS).setOnClickListener { onClick("QRIS") }
        dialogView.findViewById<Button>(R.id.btnDANA).setOnClickListener { onClick("DANA") }
        dialogView.findViewById<Button>(R.id.btnGoPay).setOnClickListener { onClick("GoPay") }
        dialogView.findViewById<Button>(R.id.btnOVO).setOnClickListener { onClick("OVO") }
        dialogView.findViewById<TextView>(R.id.btnBatal).setOnClickListener { dialog.dismiss() }

        dialog.show()
    }

    private fun updateStatusPembayaran(transaksiId: String, metode: String, position: Int) {
        val ref = FirebaseDatabase.getInstance().getReference("laporan").child(transaksiId)
        val statusBaru = if (metode.equals("Bayar Nanti", ignoreCase = true)) "Belum Dibayar" else "Lunas"

        val updates = mapOf(
            "metodePembayaran" to metode,
            "status" to statusBaru
        )


        ref.updateChildren(updates).addOnSuccessListener {
            val laporan = listLaporan[position]
            laporan.metodePembayaran = metode
            laporan.status = statusBaru

            notifyItemChanged(position)
            Toast.makeText(context, "Pembayaran $metode berhasil", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(context, "Gagal memperbarui status pembayaran", Toast.LENGTH_SHORT).show()
        }

    }

}