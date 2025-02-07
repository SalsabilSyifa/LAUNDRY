package com.example.laundry

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.laundry.modeldata.modelpegawai
import com.google.firebase.database.FirebaseDatabase

class tambah_pegawai : AppCompatActivity() {
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("pegawai")
    lateinit var tv_tambah_pegawai : TextView
    lateinit var et_nama_lengkap_pegawai : EditText
    lateinit var et_alamat_pegawai : EditText
    lateinit var et_nohp_pegawai : EditText
    lateinit var et_terdaftar_pegawai : EditText
    lateinit var et_namacabang_pegawai : EditText
    lateinit var bt_simpan_pegawai : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tambah_pegawai)
        init ()

        bt_simpan_pegawai.setOnClickListener{
            cekValidasi()
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun init(){
        tv_tambah_pegawai = findViewById(R.id.tv_tambah_pegawai)
        et_nama_lengkap_pegawai = findViewById(R.id.et_namalengkap_pegawai)
        et_alamat_pegawai = findViewById(R.id.et_alamat_pegawai)
        et_nohp_pegawai = findViewById(R.id.et_nohp_pegawai)
        et_terdaftar_pegawai = findViewById(R.id.et_terdaftar_pegawai)
        et_namacabang_pegawai = findViewById(R.id.et_namacabang_pegawai)
        bt_simpan_pegawai = findViewById(R.id.bt_simpan_pegawai)
    }

    fun cekValidasi() {
        val nama = et_nama_lengkap_pegawai.text.toString()
        val alamat = et_alamat_pegawai.text.toString()
        val nohp = et_nohp_pegawai.text.toString()
        val terdaftar = et_terdaftar_pegawai.text.toString()
        val branch = et_namacabang_pegawai.text.toString()
        //validasi data


            if (nama.isEmpty()) {
                et_nama_lengkap_pegawai.error = getString(R.string.validasi_nama_pegawai)
                Toast.makeText(this, getString(R.string.validasi_nama_pegawai), Toast.LENGTH_SHORT).show()
                et_nama_lengkap_pegawai.requestFocus()
                return
            }
            if (alamat.isEmpty()) {
                et_alamat_pegawai.error = getString(R.string.validasi_alamat_pegawai)
                Toast.makeText(this, getString(R.string.validasi_alamat_pegawai), Toast.LENGTH_SHORT).show()
                et_alamat_pegawai.requestFocus()
                return
            }

            if (nohp.isEmpty()) {
                et_nohp_pegawai.error = getString(R.string.validasi_nohp_pegawai)
                Toast.makeText(this, getString(R.string.validasi_nohp_pegawai), Toast.LENGTH_SHORT).show()
                et_nohp_pegawai.requestFocus()
                return
            }

            if (terdaftar.isEmpty()) {
                et_terdaftar_pegawai.error = getString(R.string.validasi_terdaftar_pegawai)
                Toast.makeText(this, getString(R.string.validasi_terdaftar_pegawai), Toast.LENGTH_SHORT).show()
                et_terdaftar_pegawai.requestFocus()
                return
            }

            if (branch.isEmpty()) {
                et_namacabang_pegawai.error = getString(R.string.validasi_namacabang_pegawai)
                Toast.makeText(this, getString(R.string.validasi_namacabang_pegawai), Toast.LENGTH_SHORT).show()
                et_namacabang_pegawai.requestFocus()
                return
        }
    simpan()
    }


    fun simpan() {
        val pegawaiBaru = myRef.push()
        val pegawaiId = pegawaiBaru.key
        val data = modelpegawai(
            pegawaiId.toString(),
            et_nama_lengkap_pegawai.text.toString(),
            et_alamat_pegawai.text.toString(),
            et_nohp_pegawai.text.toString(),
            et_terdaftar_pegawai.text.toString(),
            et_namacabang_pegawai.text.toString(),
            "timestamp"
        )

        pegawaiBaru.setValue(data)
            .addOnSuccessListener {
                Toast.makeText(this, getString(R.string.sukses_simpan_pelanggan), Toast.LENGTH_SHORT).show()

                val resultIntent = Intent()
                resultIntent.putExtra("pegawai_id", pegawaiId)
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
            .addOnFailureListener{
                Toast.makeText(this,getString(R.string.gagal_simpan_pelanggan), Toast.LENGTH_SHORT).show()

            }
    }
}