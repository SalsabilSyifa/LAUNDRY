package com.example.laundry

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.laundry.adapter.adapter_transaksi_tambahan
import com.example.laundry.modeldata.modeltambahan

class Activity_transaksi : AppCompatActivity() {
    private lateinit var btnPilihPelanggan : Button
    private lateinit var btnPilihLayanan : Button
    private lateinit var btnTambahan : Button
    private lateinit var btnProses : Button
    private lateinit var tvTRANSAKSI_DATA_pelanggan_nama : TextView
    private lateinit var tvTRANSAKSI_DATA_pelanggan_nohp : TextView
    private lateinit var tvTRANSAKSI_layanan_nama : TextView
    private lateinit var tvTRANSAKSI_layanan_harga : TextView
    private lateinit var rvTRANSAKSI_LayananTambahan : RecyclerView
    private val dataList = mutableListOf<modeltambahan>()
    private lateinit var adapter: adapter_transaksi_tambahan


    private val pilihPelanggan = 1
    private val pilihLayanan = 2
    private val pilihLayananTambahan = 3

    private var idPelanggan: String=""
    private var namaPelanggan: String=""
    private var nohp: String=""
    private var id_layanan: String=""
    private var namaLayanan: String=""
    private var hargaLayanan: String=""

    private var tv_nama_tambahan2: String=""
    private var tv_harga2: String=""

    private var idPegawai: String=""
    private lateinit var sharedPref: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_transaksi)

        init()


        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = false
        rvTRANSAKSI_LayananTambahan.layoutManager = layoutManager


        btnPilihPelanggan.setOnClickListener {
            val intent = Intent(this, Activity_pilihpelanggan:: class.java)
            startActivityForResult(intent,pilihPelanggan)
        }
        btnPilihLayanan.setOnClickListener {
            val intent = Intent(this, Activity_pilihlayanan:: class.java)
            startActivityForResult(intent,pilihLayanan)
        }
        btnProses.setOnClickListener {
            if (namaPelanggan.isEmpty() || namaLayanan.isEmpty()) {
                Toast.makeText(this, getString(R.string.validasiproses), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Konversi harga utama ke integer
            val hargaUtamaInt = hargaLayanan.replace(".", "").toIntOrNull() ?: 0

            // Format data tambahan
            val tambahanListStr = dataList.map {
                "${it.nama_tambahan}|${it.harga_tambahan}|${it.deskripsi_tambahan ?: "-"}"
            }

            val intent = Intent(this, konfirmasi_data::class.java).apply {
                putExtra("namaPelanggan", namaPelanggan)
                putExtra("nohp", nohp)
                putExtra("namaLayanan", namaLayanan)
                putExtra("hargaUtama", hargaUtamaInt)
                putStringArrayListExtra("layananTambahan", ArrayList(tambahanListStr))
            }

            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    fun init(){
        btnPilihPelanggan = findViewById(R.id.btnPilihPelanggan)
        btnPilihLayanan = findViewById(R.id.btnPilihLayanan)
        btnTambahan = findViewById(R.id.btnTambahan)
        btnProses = findViewById(R.id.btnProses)
        tvTRANSAKSI_DATA_pelanggan_nama = findViewById(R.id.tvTRANSAKSI_DATA_pelanggan_nama)
        tvTRANSAKSI_DATA_pelanggan_nohp = findViewById(R.id.tvTRANSAKSI_DATA_pelanggan_nohp)
        tvTRANSAKSI_layanan_nama = findViewById(R.id.tvTRANSAKSI_layanan_nama)
        tvTRANSAKSI_layanan_harga = findViewById(R.id.tvTRANSAKSI_layanan_harga)
        rvTRANSAKSI_LayananTambahan = findViewById(R.id.rvTRANSAKSI_LayananTambahan)



        adapter = adapter_transaksi_tambahan(dataList) { itemToDelete ->
            val index = dataList.indexOf(itemToDelete)
            if (index != -1) {
                dataList.removeAt(index)
                adapter.notifyItemRemoved(index)
            }
        }
        rvTRANSAKSI_LayananTambahan.adapter = adapter
        rvTRANSAKSI_LayananTambahan.layoutManager = LinearLayoutManager(this)

        tvTRANSAKSI_DATA_pelanggan_nama.text = ": $namaPelanggan"
        tvTRANSAKSI_DATA_pelanggan_nohp.text = ": $nohp"

        tvTRANSAKSI_layanan_nama.text = ": $namaLayanan"
        tvTRANSAKSI_layanan_harga.text = ": $hargaLayanan"



        btnTambahan.setOnClickListener {
            val intent = Intent(this, activity_pilihtambahan::class.java)
            startActivityForResult(intent, pilihLayananTambahan)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null) {
            when (requestCode) {
                pilihPelanggan -> {
                    idPelanggan = data.getStringExtra("idPelanggan").toString()
                    namaPelanggan = data.getStringExtra("nama").toString()
                    nohp = data.getStringExtra("nohp").toString()

                    tvTRANSAKSI_DATA_pelanggan_nama.text = ": $namaPelanggan"
                    tvTRANSAKSI_DATA_pelanggan_nohp.text = ": $nohp"


                    val pesan = getString(R.string.pelanggandipilih, namaPelanggan)
                    Toast.makeText(this, pesan, Toast.LENGTH_SHORT).show()


                }

                pilihLayanan -> {
                    id_layanan = data.getStringExtra("id_layanan").toString()
                    namaLayanan = data.getStringExtra("tv_nama_layanan").toString()
                    hargaLayanan = data.getStringExtra("tv_harga").toString()


                    tvTRANSAKSI_layanan_nama.text = ": $namaLayanan"
                    tvTRANSAKSI_layanan_harga.text = ": $hargaLayanan"

                    val pesan = getString(R.string.layanandipilih, namaLayanan)
                    Toast.makeText(this, pesan, Toast.LENGTH_SHORT).show()
                }
                pilihLayananTambahan -> {
                    tv_nama_tambahan2 = data.getStringExtra("nama_tambahan").toString()
                    tv_harga2 = data.getStringExtra("harga_tambahan").toString()

                    val namaTambahan = data.getStringExtra("nama_tambahan").toString()
                    val hargaTambahan = data.getStringExtra("harga_tambahan").toString()
                    val deskripsiTambahan = data.getStringExtra("deskripsi_tambahan").toString() // Tambahkan ini

                    val tambahan = modeltambahan(
                        id_tambahan = null,
                        nama_tambahan = namaTambahan,
                        deskripsi_tambahan = deskripsiTambahan, // Tambahkan ke sini juga
                        harga_tambahan = hargaTambahan
                    )


                    dataList.add(tambahan)
                    adapter.notifyItemInserted(dataList.size - 1)
                    val pesan = getString(R.string.tambahandipilih, namaTambahan)
                    Toast.makeText(this, pesan, Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

}