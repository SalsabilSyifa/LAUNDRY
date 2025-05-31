package com.example.laundry

import android.content.Intent
import android.icu.text.NumberFormat
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.laundry.adapter.adapter_pembayaran
import com.example.laundry.modeldata.modeltambahan
import java.text.SimpleDateFormat
import java.util.*

class pembayaran : AppCompatActivity() {

    private fun formatRupiah(number: Int): String {
        val localeID = Locale("in", "ID")
        val format = NumberFormat.getCurrencyInstance(localeID)
        return format.format(number)
    }

    private fun buildPesanWhatsApp(
        namaPelanggan: String,
        layanan: String,
        harga: String,
        total: String
    ): String {
        return """
        WoHalo $namaPelanggan 👋

        Berikut rincian laundry Anda:
        * Layanan Utama: $layanan
        * Harga: $harga

        Rincian Tambahan:
        Total Bayar: $total

        Terima kasih telah menggunakan layanan wazh Laundry 💟
    """.trimIndent()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pembayaran)

        // Bind view
        val idTransaksi = findViewById<TextView>(R.id.id_transaksi_pembayaran)
        val tanggal = findViewById<TextView>(R.id.tv_tanggal_pembayaran)
        val namaPelanggan = findViewById<TextView>(R.id.tv_namapelanggan)
        val namaPegawai = findViewById<TextView>(R.id.tv_namapegawa)
        val layananUtama = findViewById<TextView>(R.id.tvLayananUtama)
        val hargaUtama = findViewById<TextView>(R.id.tvHargaUtama)
        val subtotalTambahan = findViewById<TextView>(R.id.tv_subtotal_hargatambahan)
        val totalBayar = findViewById<TextView>(R.id.totalBayar)
        val recyclerTambahan = findViewById<RecyclerView>(R.id.rv_tambahan_pembayaran)

        // Generate ID Transaksi dan Tanggal Sekarang
        val generatedId = "TRX" + System.currentTimeMillis().toString().takeLast(6)
        val currentDate = SimpleDateFormat("dd-MM-yyyy", Locale("in", "ID")).format(Date())

        // Ambil data dari intent
        val dataNamaPelanggan = intent.getStringExtra("nama_pelanggan") ?: "-"
        val dataNamaPegawai = intent.getStringExtra("nama_pegawai") ?: "-"
        val dataLayananUtama = intent.getStringExtra("tv_nama_layanan") ?: "-"
        val dataHargaUtama = intent.getDoubleExtra("tv_harga", 0.0).toInt()
        val tambahanListString = intent.getStringArrayListExtra("layanan_tambahan") ?: arrayListOf()

        val tambahanList = tambahanListString.mapNotNull {
            val parts = it.split("|")
            if (parts.size >= 3) {
                modeltambahan(
                    id_tambahan = parts[0],
                    nama_tambahan = parts[1],
                    harga_tambahan = parts[2],
                    deskripsi_tambahan = if (parts.size > 3) parts[3] else null
                )
            } else {
                null
            }
        }

        // Hitung subtotal tambahan
        val subtotal = tambahanList.sumOf {
            val hargaStr = it.harga_tambahan
                ?.replace("Rp", "")
                ?.replace(".", "")
                ?.replace(",", ".")
                ?.trim()

            hargaStr?.toDoubleOrNull()?.toInt() ?: 0
        }


        val total = dataHargaUtama + subtotal

        // Set ke tampilan
        idTransaksi.text = generatedId
        tanggal.text = currentDate
        namaPelanggan.text = dataNamaPelanggan
        namaPegawai.text = dataNamaPegawai
        layananUtama.text = dataLayananUtama
        hargaUtama.text = formatRupiah(dataHargaUtama)
        subtotalTambahan.text = formatRupiah(subtotal)
        totalBayar.text = formatRupiah(total)

        // Tampilkan ke RecyclerView
        recyclerTambahan.layoutManager = LinearLayoutManager(this)
        recyclerTambahan.adapter = adapter_pembayaran(tambahanList.toMutableList())
        Log.d("PembayaranActivity", "List tambahan size: ${tambahanList.size}")
        Log.d("PembayaranActivity", "Nama layanan: $dataLayananUtama, Harga: $dataHargaUtama")
        Log.d("Adapter", "Data: ${tambahanList}")


        val btnWA = findViewById<Button>(R.id.btnWA)
        btnWA.setOnClickListener {
            val pesan = buildPesanWhatsApp(
                namaPelanggan.text.toString(),
                layananUtama.text.toString(),
                hargaUtama.text.toString(),
                totalBayar.text.toString()
            )

            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://wa.me/?text=${Uri.encode(pesan)}")
            startActivity(intent)
        }

    }

}
