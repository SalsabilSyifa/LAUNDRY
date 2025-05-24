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
import com.example.laundry.modeldata.modelTransaksiTambahan
import com.example.laundry.modeldata.modeltambahan
import com.google.firebase.database.FirebaseDatabase

class activity_tambah_tambahan : AppCompatActivity() {
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("tambahan")
    lateinit var id_tambahan : TextView
    lateinit var et_tambah_tambahan : EditText
    lateinit var et_tambahan_harga : EditText
    lateinit var bt_simpan_tambahan : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tambah_tambahan)
        init()
        bt_simpan_tambahan.setOnClickListener{
            cekValidasi()
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    fun init() {
        id_tambahan = findViewById(R.id.tv_tambahan_judul)
        et_tambah_tambahan = findViewById(R.id.et_tambahan)
        et_tambahan_harga = findViewById(R.id.et_harga)
        bt_simpan_tambahan = findViewById(R.id.bt_simpan_tambahan)
    }
    fun cekValidasi(){
        val namatambahan = et_tambah_tambahan.text.toString()
        val harga = et_tambahan_harga.text.toString()

        if ( namatambahan.isEmpty()){
            et_tambah_tambahan.error = "tambahan tidak boleh kosong"
            Toast.makeText(this, "tambahan tidak boleh kosong", Toast.LENGTH_SHORT).show()
            et_tambah_tambahan.requestFocus()
            return
        }
        if ( harga.isEmpty()){
            et_tambahan_harga.error = "harga tidak boleh kosong"
            Toast.makeText(this, "harga tidak boleh kosong", Toast.LENGTH_SHORT).show()
            et_tambahan_harga.requestFocus()
            return
        }
        simpan()
        }

    fun simpan(){
        val tambahanBaru = myRef.push()
        val tambahanId = tambahanBaru.key
        val data = modeltambahan(
            tambahanId.toString(),
            et_tambah_tambahan.text.toString(),
            et_tambahan_harga.text.toString(),
        )
        tambahanBaru.setValue(data)
            .addOnSuccessListener {
                Toast.makeText(this,"sukses simpan tambahan", Toast.LENGTH_SHORT).show()

                val resultIntent = Intent()
                resultIntent.putExtra("tambahan_id",tambahanId)
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "gagal simpan tambahan", Toast.LENGTH_SHORT).show()
            }
    }
    }
