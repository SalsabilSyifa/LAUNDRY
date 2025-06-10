package com.example.laundry

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
import com.example.laundry.modeldata.modelcabang
import com.example.laundry.modeldata.modelpelanggan
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class activity_tambah_cabang : AppCompatActivity() {
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("cabang")
    lateinit var tv_tambah_cabang: TextView
    lateinit var et_namacabang: EditText
    lateinit var et_alamat: EditText
    lateinit var et_layanan: EditText
    lateinit var bt_simpan_cabang: Button

    var isEdit = false

    var id_cabang: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tambah_cabang)
        init()
        getData()

        bt_simpan_cabang.setOnClickListener {
            cekValidasi()
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    fun init() {
        tv_tambah_cabang = findViewById(R.id.tv_tambah_cabang)
        et_namacabang = findViewById(R.id.et_namacabang)
        et_alamat = findViewById(R.id.et_alamat)
        et_layanan = findViewById(R.id.et_layanan)
        bt_simpan_cabang = findViewById(R.id.bt_simpan_cabang)
    }
    fun getData() {
        id_cabang = intent.getStringExtra("id") ?: ""
        Log.d("DEBUG", "id_cabang: $id_cabang")

        if (id_cabang.isNotEmpty()) {
            isEdit = true
            tv_tambah_cabang.text = "Edit Cabang"
            bt_simpan_cabang.text = "Edit"
            mati()
            database.getReference("cabang").child(id_cabang).get()
                .addOnSuccessListener { snapshot ->
                    val data = snapshot.getValue(modelcabang::class.java)
                    if (data != null) {
                        et_namacabang.setText(data.tv_nama_cabang)
                        et_alamat.setText(data.tv_alamat_cabang)
                        et_layanan.setText(data.tv_layanan_cabang)
                    } else {
                        Log.e("DEBUG", "Data cabang tidak ditemukan!")
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Gagal memuat data cabang", Toast.LENGTH_SHORT).show()
                }
        } else {
            isEdit = false
            tv_tambah_cabang.text = getString(R.string.tambah_cabang)
            bt_simpan_cabang.text = getString(R.string.tambah_pelanggan_simpan)
            hidup()
            et_namacabang.requestFocus()
        }
    }
    fun hidup() {
        et_namacabang.isEnabled = true
        et_alamat.isEnabled = true
        et_layanan.isEnabled = true
    }
    fun mati() {
        et_namacabang.isEnabled = false
        et_alamat.isEnabled = false
        et_layanan.isEnabled = false
    }


    fun update() {
        if (id_cabang.isEmpty()) {
            Toast.makeText(this, "Error: ID cabang tidak ditemukan!", Toast.LENGTH_SHORT).show()
            return
        }

        val cabangRef = database.getReference("cabang").child(id_cabang)
        val updateData = mutableMapOf<String, Any>()
        updateData["tv_nama_cabang"] = et_namacabang.text.toString()
        updateData["tv_alamat_cabang"] = et_alamat.text.toString()
        updateData["tv_layanan_cabang"] = et_layanan.text.toString()

        cabangRef.updateChildren(updateData).addOnSuccessListener {
            Toast.makeText(
                this@activity_tambah_cabang,
                getString(R.string.berhasildiperbarui),
                Toast.LENGTH_SHORT
            ).show()
            finish()
        }.addOnFailureListener {
            Toast.makeText(
                this@activity_tambah_cabang,
                "Data Cabang Gagal Diperbarui",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    fun cekValidasi() {
        val nama = et_namacabang.text.toString()
        val alamat = et_alamat.text.toString()
        val layanan = et_layanan.text.toString()

        if (nama.isEmpty()) {
            et_namacabang.error = getString(R.string.validasi_nama_pelanggan)
            Toast.makeText(this, getString(R.string.validasi_nama_pelanggan), Toast.LENGTH_SHORT)
                .show()
            et_namacabang.requestFocus()
            return
        }

        if (alamat.isEmpty()) {
            et_alamat.error = getString(R.string.validasi_alamat_pelanggan)
            Toast.makeText(this, getString(R.string.validasi_alamat_pelanggan), Toast.LENGTH_SHORT)
                .show()
            et_alamat.requestFocus()
            return
        }

        if (layanan.isEmpty()) {
            et_layanan.error = getString(R.string.validasi_nama_layanan)
            Toast.makeText(this, getString(R.string.validasi_nama_layanan), Toast.LENGTH_SHORT).show()
            et_layanan.requestFocus()
            return
        }

        if (bt_simpan_cabang.text.equals(getString(R.string.tambah_pelanggan_simpan))) {
            simpan()
        }else if(bt_simpan_cabang.text.equals(getString(R.string.edit))){
            hidup()
            et_namacabang.requestFocus()
            bt_simpan_cabang.text=getString(R.string.perbarui)
        }else if (bt_simpan_cabang.text.equals(getString(R.string.perbarui))) {
            update()
        }

    }
    fun simpan() {
        val cabangBaru = myRef.push()
        val cabangId = cabangBaru.key ?: return
        val data = modelcabang(
            id_cabang = cabangId,
            tv_nama_cabang = et_namacabang.text.toString(),
            tv_alamat_cabang = et_alamat.text.toString(),
            tv_layanan_cabang = et_layanan.text.toString(),
        )

        cabangBaru.setValue(data)
            .addOnSuccessListener {
                Toast.makeText(this, getString(R.string.validasicabangsukses), Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Gagal menyimpan cabang", Toast.LENGTH_SHORT).show()
            }
    }
}