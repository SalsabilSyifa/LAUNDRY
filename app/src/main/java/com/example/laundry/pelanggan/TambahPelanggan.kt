package com.example.laundry.pelanggan

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
import com.example.laundry.R
import com.example.laundry.modeldata.modelpelanggan
import com.google.firebase.Firebase
import com.google.firebase.database.FirebaseDatabase
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TambahPelanggan : AppCompatActivity() {
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("pelanggan")
    lateinit var tv_tambah_pengguna : TextView
    lateinit var et_namalengkap : EditText
    lateinit var et_alamat : EditText
    lateinit var et_nohp : EditText
    lateinit var et_terdaftar : EditText
    lateinit var bt_simpan : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tambah_pelanggan)

        init()

        bt_simpan.setOnClickListener{
            cekValidasi()
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun init(){
        tv_tambah_pengguna = findViewById(R.id.tv_tambah_pengguna)
        et_namalengkap = findViewById(R.id.et_namalengkap)
        et_alamat = findViewById(R.id.et_alamat)
        et_nohp = findViewById(R.id.et_nohp)
        et_terdaftar = findViewById(R.id.et_terdaftar)
        bt_simpan = findViewById(R.id.bt_simpan)
    }

    fun cekValidasi(){
        val nama = et_namalengkap.text.toString()
        val alamat = et_alamat.text.toString()
        val nohp = et_nohp.text.toString()
        val terdaftar = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        et_terdaftar.setText(terdaftar)
        //validasi data


            if (nama.isEmpty()) {
                et_namalengkap.error = getString(R.string.validasi_nama_pelanggan)
                Toast.makeText(this, getString(R.string.validasi_nama_pelanggan), Toast.LENGTH_SHORT).show()
                et_namalengkap.requestFocus()
                return
            }

            if (alamat.isEmpty()) {
                et_alamat.error = getString(R.string.validasi_alamat_pelanggan)
                Toast.makeText(this, getString(R.string.validasi_alamat_pelanggan), Toast.LENGTH_SHORT).show()
                et_alamat.requestFocus()
                return
            }

        if (!nohp.matches(Regex("^[0-9]+$"))) {
            et_nohp.error = "Nomor HP harus berupa angka"
            Toast.makeText(this, "Nomor HP harus berupa angka", Toast.LENGTH_SHORT).show()
            et_nohp.requestFocus()
            return
        }

            if (terdaftar.isEmpty()) {
                et_terdaftar.isFocusable = false
                et_terdaftar.isClickable = false
                return
        }
    simpan()
    }


        fun simpan() {
            val pelangganBaru = myRef.push()
            val pelangganId = pelangganBaru.key
            val data = modelpelanggan(
                pelangganId.toString(),
                et_namalengkap.text.toString(),
                et_alamat.text.toString(),
                et_nohp.text.toString(),
                et_terdaftar.text.toString(),
                "timestamp"
            )
            pelangganBaru.setValue(data)
                .addOnSuccessListener {
                    Toast.makeText(this, getString(R.string.sukses_simpan_pelanggan), Toast.LENGTH_SHORT).show()

                    val resultIntent = Intent()
                    resultIntent.putExtra("pelanggan_id",pelangganId)
                    setResult(Activity.RESULT_OK, resultIntent)
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, getString(R.string.gagal_simpan_pelanggan), Toast.LENGTH_SHORT).show()
                }
        }



    }

