package com.example.laundry

import android.content.Intent
import android.icu.text.NumberFormat
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.laundry.adapter.adapter_konfirmasi_data
import com.example.laundry.modeldata.modelLaporan
import com.example.laundry.modeldata.modeltambahan
import com.google.firebase.database.FirebaseDatabase
import java.sql.Date
import java.util.Locale

class konfirmasi_data : AppCompatActivity() {
    private var hargaUtama: Int = 0
    private var total: Int = 0

    object DataLaporan {
        val list: MutableList<modelLaporan> = mutableListOf()
    }

    fun formatRupiah(number: Int): String {
        val localeID = Locale("in", "ID")
        val format = NumberFormat.getCurrencyInstance(localeID)
        return format.format(number)
    }

    private fun showMetodePembayaranDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_metode_pembayaran, null)
        val dialog = android.app.AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(true)
            .create()

        fun pindahKeStruk(metode: String) {
            val nama = textNama.text.toString()
            val hp = textHp.text.toString()
            val layanan = textLayanan.text.toString()
            val tambahanList = intent.getStringArrayListExtra("layananTambahan") ?: arrayListOf()


            val currentDate = SimpleDateFormat("dd-MM-yyyy  HH:mm:ss", Locale("in", "ID")).format(java.util.Date())
            val tanggalSekarang = currentDate.format(java.util.Date())
            val idTransaksi = "TRX" + System.currentTimeMillis().toString().takeLast(6)

            val tambahanGabung = tambahanList.joinToString {
                val parts = it.split("|")
                if (parts.size >= 2) parts[1] else ""
            }


            val statusBaru = if (metode.equals("Bayar Nanti", ignoreCase = true)) "Belum Dibayar" else "Sudah Dibayar"

            val laporan = modelLaporan(
                idTransaksi = idTransaksi,
                namaPelanggan = nama,
                tanggal = tanggalSekarang,
                layananUtama = layanan,
                tambahan = tambahanList,
                totalBayar = total.toString(),
                metodePembayaran = metode,
                status = statusBaru

            )

            DataLaporan.list.add(laporan)

            val tambahanModelList = tambahanList.mapNotNull {
                val parts = it.split("|")
                if (parts.size >= 3) {
                    modeltambahan(parts[0], parts[1])
                } else null
            }

            // Kirim ke halaman pembayaran
            val intent = Intent(this, pembayaran::class.java).apply {
                putExtra("metode_pembayaran", metode)
                putExtra("nama_pelanggan", nama)
                putExtra("noHp", hp)
                putExtra("tv_nama_layanan", layanan)
                putExtra("tv_harga", hargaUtama.toDouble())
                putExtra("total_bayar", total.toDouble())

                val tambahanListString = tambahanModelList.map {
                    "${it.id_tambahan}|${it.nama_tambahan}|${it.harga_tambahan}|${it.deskripsi_tambahan ?: "-"}"
                }


                putStringArrayListExtra("layanan_tambahan", ArrayList(tambahanListString))
            }
            val laporanRef = FirebaseDatabase.getInstance().getReference("laporan")
            laporanRef.child(idTransaksi).setValue(laporan)

            startActivity(intent)
            dialog.dismiss()
            finish()
        }


        dialogView.findViewById<Button>(R.id.buttonBayarNanti).setOnClickListener {
            pindahKeStruk("Bayar Nanti")
        }

        dialogView.findViewById<Button>(R.id.buttonTunai).setOnClickListener {
            pindahKeStruk("Tunai")
        }

        dialogView.findViewById<Button>(R.id.buttonQRIS).setOnClickListener {
            pindahKeStruk("QRIS")
        }

        dialogView.findViewById<Button>(R.id.buttonDANA).setOnClickListener {
            pindahKeStruk("DANA")
        }

        dialogView.findViewById<Button>(R.id.buttonGoPay).setOnClickListener {
            pindahKeStruk("GoPay")
        }

        dialogView.findViewById<Button>(R.id.buttonOVO).setOnClickListener {
            pindahKeStruk("OVO")
        }

        dialogView.findViewById<TextView>(R.id.buttonBatal).setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private lateinit var textNama: TextView
    private lateinit var textHp: TextView
    private lateinit var textLayanan: TextView
    private lateinit var textHargaUtama: TextView
    private lateinit var textTotalBayar: TextView
    private lateinit var recyclerTambahan: RecyclerView
    private lateinit var btnBayar: Button
    private lateinit var btnBatal: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_konfirmasi_data)

        // Bind Views
        textNama = findViewById(R.id.tv_konfirmasi_pelanggan_nama)
        textHp = findViewById(R.id.tv_konfirmasi_pelanggan_nohp)
        textLayanan = findViewById(R.id.tv_konfirmasi_layanan_nama)
        textHargaUtama = findViewById(R.id.tv_konfirmasi_layanan_harga)
        textTotalBayar = findViewById(R.id.tv_total)
        recyclerTambahan = findViewById(R.id.rv_konfirmasi_LayananTambahan)
        btnBayar = findViewById(R.id.btnPembayaran)
        btnBatal = findViewById(R.id.btnBatal)

        // Ambil data dari intent
        val nama = intent.getStringExtra("namaPelanggan")
        val hp = intent.getStringExtra("nohp")
        val layanan = intent.getStringExtra("namaLayanan")
        hargaUtama = intent.getIntExtra("hargaUtama", 0)

        val tambahanListString = intent.getStringArrayListExtra("layananTambahan")

        // Tampilkan data utama
        textNama.text = nama ?: "nama tidak tersedia"
        textHp.text = hp ?: "-"
        textLayanan.text = layanan ?: "layanan tidak tersedia"
        textHargaUtama.text = formatRupiah(hargaUtama)


        val tambahanList = tambahanListString?.mapNotNull {
            val parts = it.split("|")
            return@mapNotNull if (parts.size == 3) {
                modeltambahan(
                    id_tambahan = parts[0],
                    nama_tambahan = parts[0],
                    harga_tambahan = parts[1]
                )
            } else {
                Log.e("KONVERSI_ERROR", "Format tidak sesuai: $it")
                null
            }
        } ?: listOf()


        // Hitung total
        total = hargaUtama
        Log.d("TOTAL_DEBUG", "Harga utama: $hargaUtama")

        for (tambahan in tambahanList) {
            val harga = tambahan.harga_tambahan
                ?.replace("Rp", "", ignoreCase = true)
                ?.replace(".", "")
                ?.replace(",00", "")
                ?.trim()
                ?.toIntOrNull() ?: 0


            Log.d("TOTAL_DEBUG", "Tambahan: ${tambahan.nama_tambahan} = $harga")
            total += harga
        }

        Log.d("TOTAL_DEBUG", "Total akhir: $total")


        textTotalBayar.text = formatRupiah(total)

        // Setup RecyclerView
        recyclerTambahan.layoutManager = LinearLayoutManager(this)
        recyclerTambahan.adapter = adapter_konfirmasi_data(ArrayList(tambahanList))

        // Tombol aksi
        btnBayar.setOnClickListener {
            showMetodePembayaranDialog()
        }

        btnBatal.setOnClickListener {
            finish()
        }
    }
}