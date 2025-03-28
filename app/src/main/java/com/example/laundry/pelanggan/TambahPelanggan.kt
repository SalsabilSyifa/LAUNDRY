package com.example.laundry.pelanggan
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.laundry.R
import com.example.laundry.modeldata.modelpegawai
import com.example.laundry.modeldata.modelpelanggan
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TambahPelanggan : AppCompatActivity() {
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("pelanggan")
    lateinit var tv_tambah_pengguna: TextView
    lateinit var et_namalengkap: EditText
    lateinit var et_alamat: EditText
    lateinit var et_nohp: EditText
    lateinit var bt_simpan: Button

    var isEdit = false

    var id_pelanggan: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tambah_pelanggan)
        init()
        getData()

        bt_simpan.setOnClickListener {
            cekValidasi()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun init() {
        tv_tambah_pengguna = findViewById(R.id.tv_tambah_pengguna)
        et_namalengkap = findViewById(R.id.et_namalengkap)
        et_alamat = findViewById(R.id.et_alamat)
        et_nohp = findViewById(R.id.et_nohp)
        bt_simpan = findViewById(R.id.bt_simpan)
    }

    fun getData() {
        id_pelanggan = intent.getStringExtra("id") ?: ""
        Log.d("DEBUG", "id_pelanggan: $id_pelanggan")

        if (id_pelanggan.isNotEmpty()) {
            isEdit = true
            tv_tambah_pengguna.text = "Edit Pelanggan"
            bt_simpan.text = "Edit"
            hidup()
            database.getReference("pelanggan").child(id_pelanggan).get()
                .addOnSuccessListener { snapshot ->
                    val data = snapshot.getValue(modelpelanggan::class.java)
                    if (data != null) {
                        et_namalengkap.setText(data.nama)
                        et_alamat.setText(data.alamat)
                        et_nohp.setText(data.nohp)
                    } else {
                        Log.e("DEBUG", "Data pelanggan tidak ditemukan!")
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Gagal memuat data pelanggan", Toast.LENGTH_SHORT).show()
                }
        } else {
            isEdit = false
            tv_tambah_pengguna.text = "Tambah Pelanggan"
            bt_simpan.text = "Simpan"
            hidup()
            et_namalengkap.requestFocus()
        }
    }

    fun hidup() {
        et_namalengkap.isEnabled = true
        et_alamat.isEnabled = true
        et_nohp.isEnabled = true
    }

    fun update() {
        if (id_pelanggan.isEmpty()) {
            Toast.makeText(this, "Error: ID pelanggan tidak ditemukan!", Toast.LENGTH_SHORT).show()
            return
        }

        val pelangganRef = database.getReference("pelanggan").child(id_pelanggan)
        val updateData = mutableMapOf<String, Any>()
        updateData["nama"] = et_namalengkap.text.toString()
        updateData["alamat"] = et_alamat.text.toString()
        updateData["nohp"] = et_nohp.text.toString()

        pelangganRef.updateChildren(updateData).addOnSuccessListener {
            Toast.makeText(
                this@TambahPelanggan,
                "Data Pelanggan Berhasil Diperbarui",
                Toast.LENGTH_SHORT
            ).show()
            finish()
        }.addOnFailureListener {
            Toast.makeText(
                this@TambahPelanggan,
                "Data Pelanggan Gagal Diperbarui",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun cekValidasi() {
        val nama = et_namalengkap.text.toString()
        val alamat = et_alamat.text.toString()
        val nohp = et_nohp.text.toString()
        val terdaftar = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault()).format(Date())
        //validasi data

        if (nama.isEmpty()) {
            et_namalengkap.error = getString(R.string.validasi_nama_pelanggan)
            Toast.makeText(this, getString(R.string.validasi_nama_pelanggan), Toast.LENGTH_SHORT)
                .show()
            et_namalengkap.requestFocus()
            return
        }

        if (alamat.isEmpty()) {
            et_alamat.error = getString(R.string.validasi_alamat_pelanggan)
            Toast.makeText(this, getString(R.string.validasi_alamat_pelanggan), Toast.LENGTH_SHORT)
                .show()
            et_alamat.requestFocus()
            return
        }

        if (!nohp.matches(Regex("^[0-9]+$"))) {
            et_nohp.error = "Nomor HP harus berupa angka"
            Toast.makeText(this, "Nomor HP harus berupa angka", Toast.LENGTH_SHORT).show()
            et_nohp.requestFocus()
            return
        }

        if (isEdit) {
            update()
        } else {
            simpan(terdaftar)
        }
    }

    fun simpan(terdaftar: String) {
        val pelangganBaru = myRef.push()
        val pelangganId = pelangganBaru.key ?: return
        val data = modelpelanggan(
            id_pelanggan = pelangganId,
            nama = et_namalengkap.text.toString(),
            alamat = et_alamat.text.toString(),
            nohp = et_nohp.text.toString(),
            terdaftar = terdaftar
        )

        pelangganBaru.setValue(data)
            .addOnSuccessListener {
                Toast.makeText(this, "Pelanggan berhasil disimpan", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Gagal menyimpan pelanggan", Toast.LENGTH_SHORT).show()
            }
    }
}

