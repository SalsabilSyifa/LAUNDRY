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
import com.example.laundry.modeldata.modelTransaksiTambahan
import com.example.laundry.modeldata.modelpelanggan

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
    private val dataList = mutableListOf<modelTransaksiTambahan>()

    private val pilihPelanggan = 1
    private val pilihLayanan = 2
    private val pilihLayananTambahan = 3

    private var idPelanggan: String=""
    private var namaPelanggan: String=""
    private var nohp: String=""
    private var id_layanan: String=""
    private var namaLayanan: String=""
    private var hargaLayanan: String=""
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


        tvTRANSAKSI_DATA_pelanggan_nama.text = "Nama Pelanggan : $namaPelanggan"
        tvTRANSAKSI_DATA_pelanggan_nohp.text = "No HP : $nohp"

        tvTRANSAKSI_layanan_nama.text = "Nama Layanan : $namaLayanan"
        tvTRANSAKSI_layanan_harga.text = "Harga : $hargaLayanan"
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null) {
            when (requestCode) {
                pilihPelanggan -> {
                    idPelanggan = data.getStringExtra("idPelanggan").toString()
                    namaPelanggan = data.getStringExtra("nama").toString()
                    nohp = data.getStringExtra("nohp").toString()

                    tvTRANSAKSI_DATA_pelanggan_nama.text = "Nama Pelanggan : $namaPelanggan"
                    tvTRANSAKSI_DATA_pelanggan_nohp.text = "No HP : $nohp"
                }

                pilihLayanan -> {
                    id_layanan = data.getStringExtra("id_layanan").toString()
                    namaLayanan = data.getStringExtra("tv_nama_layanan").toString()
                    hargaLayanan = data.getStringExtra("tv_harga").toString()


                    tvTRANSAKSI_layanan_nama.text = "Nama Layanan : $namaLayanan"
                    tvTRANSAKSI_layanan_harga.text = "Harga : $hargaLayanan"

                    Toast.makeText(this, "Layanan Dipilih: $namaLayanan", Toast.LENGTH_SHORT).show()
                }
            }
        } else if (resultCode == RESULT_CANCELED) {
            when (requestCode) {
                pilihPelanggan -> {
                    Toast.makeText(this, "Batal Memilih Pelanggan", Toast.LENGTH_SHORT).show()
                }

                pilihLayanan -> {
                    Toast.makeText(this, "Batal Memilih Layanan", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}
