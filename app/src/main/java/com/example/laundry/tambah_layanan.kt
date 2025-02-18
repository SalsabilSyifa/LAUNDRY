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
import com.example.laundry.modeldata.modellayanan
import com.google.firebase.database.FirebaseDatabase

class tambah_layanan : AppCompatActivity() {
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("layanan")
    lateinit var tv_tambah_layanan : TextView
    lateinit var et_namalayanan : EditText
    lateinit var et_harga : EditText
    lateinit var et_cabang : EditText
    lateinit var bt_simpan_layanan : Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tambah_layanan)

        init()

        bt_simpan_layanan.setOnClickListener{
            cekValidasi()
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun init() {
        tv_tambah_layanan = findViewById(R.id.tv_tambah_layanan)
        et_namalayanan = findViewById(R.id.et_namalayanan)
        et_harga = findViewById(R.id.et_harga)
        et_cabang = findViewById(R.id.et_cabang)
        bt_simpan_layanan = findViewById(R.id.bt_simpan_layanan)
    }

    fun cekValidasi(){
        val namalayanan = et_namalayanan.text.toString()
        val harga = et_harga.text.toString()
        val cabang = et_cabang.text.toString()
        //validasi data


        if (namalayanan.isEmpty()) {
            et_namalayanan.error = getString(R.string.validasi_nama_layanan)
            Toast.makeText(this, getString(R.string.validasi_nama_layanan), Toast.LENGTH_SHORT).show()
            et_namalayanan.requestFocus()
            return
        }

        if (harga.isEmpty()) {
            et_harga.error = getString(R.string.validasi_harga_layanan)
            Toast.makeText(this, getString(R.string.validasi_harga_layanan), Toast.LENGTH_SHORT).show()
            et_harga.requestFocus()
            return
        }

        if (cabang.isEmpty()) {
            et_cabang.error = getString(R.string.validasi_cabang_layanan)
            Toast.makeText(this, getString(R.string.validasi_cabang_layanan), Toast.LENGTH_SHORT).show()
            et_cabang.requestFocus()
            return
        }

        simpan()
        }

    fun simpan (){
        val layananBaru = myRef.push()
        val layananId = layananBaru.key
        val data = modellayanan(
            layananId.toString(),
            et_namalayanan.text.toString(),
            et_harga.text.toString(),
            et_cabang.text.toString(),
            "timestamp"
        )
        layananBaru.setValue(data)
            .addOnSuccessListener {
                Toast.makeText(this,getString(R.string.sukses_simpan_layanan), Toast.LENGTH_SHORT).show()

                val resultIntent = Intent()
                resultIntent.putExtra("layanan_id",layananId)
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
            .addOnSuccessListener {
                Toast.makeText(this, getString(R.string.gagal_simpan_layanan), Toast.LENGTH_SHORT).show()
            }
    }
    }
