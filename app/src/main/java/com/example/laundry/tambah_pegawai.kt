package com.example.laundry

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.laundry.modeldata.modelpegawai
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class tambah_pegawai : AppCompatActivity() {
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("pegawai")
    lateinit var tv_tambah_pegawai : TextView
    lateinit var et_namalengkap_pegawai : EditText
    lateinit var et_alamat_pegawai : EditText
    lateinit var et_nohp_pegawai : EditText
    lateinit var et_namacabang_pegawai : EditText
    lateinit var bt_simpan_pegawai : Button

    var id_pegawai:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tambah_pegawai)
        init ()
        getData()
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
        et_namalengkap_pegawai = findViewById(R.id.et_namalengkap_pegawai)
        et_alamat_pegawai = findViewById(R.id.et_alamat_pegawai)
        et_nohp_pegawai = findViewById(R.id.et_nohp_pegawai)
        et_namacabang_pegawai = findViewById(R.id.et_namacabang_pegawai)
        bt_simpan_pegawai = findViewById(R.id.bt_simpan_pegawai)
    }

    fun getData(){
        id_pegawai = intent.getStringExtra("id") ?: ""

        if(id_pegawai.isNotEmpty()){
            database.getReference("pegawai").child(id_pegawai).get()
                .addOnSuccessListener { snapshot ->
                    val data = snapshot.getValue(modelpegawai::class.java)
                    if(data != null){
                        et_namalengkap_pegawai.setText(data.namapegawai)
                        et_alamat_pegawai.setText(data.alamatpegawai)
                        et_nohp_pegawai.setText(data.nohppegawai)
                        et_namacabang_pegawai.setText(data.cabangpegawai)
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Gagal memuat data pegawai", Toast.LENGTH_SHORT).show()
                }
        }

        val judul = intent.getStringExtra("judul") ?: ""
        tv_tambah_pegawai.text = judul

        if(judul.equals(" Edit Pegawai", ignoreCase = true)){
            mati()
            bt_simpan_pegawai.text="Edit"
        } else {
            hidup()
            et_namalengkap_pegawai.requestFocus()
            bt_simpan_pegawai.text="Simpan"
        }
    }


    fun mati(){
        et_namalengkap_pegawai.isEnabled=false
        et_alamat_pegawai.isEnabled=false
        et_nohp_pegawai.isEnabled=false
        et_namacabang_pegawai.isEnabled=false
    }
    fun hidup(){
        et_namalengkap_pegawai.isEnabled=true
        et_alamat_pegawai.isEnabled=true
        et_nohp_pegawai.isEnabled=true
        et_namacabang_pegawai.isEnabled=true
    }

    fun update(){
        val pegawaiRef = database.getReference("pegawai").child(id_pegawai)
        val updateData = mutableMapOf<String, Any>()
        updateData["namapegawai"]= et_namalengkap_pegawai.text.toString()
        updateData["alamatpegawai"]= et_alamat_pegawai.text.toString()
        updateData["nohppegawai"]= et_nohp_pegawai.text.toString()
        updateData["cabangpegawai"]= et_namacabang_pegawai.text.toString()
        pegawaiRef.updateChildren(updateData).addOnSuccessListener {
            Toast.makeText(this@tambah_pegawai, "Data Pegawai Berhasil Diperbarui",Toast.LENGTH_SHORT).show()
            finish()
        }.addOnFailureListener{
            Toast.makeText(this@tambah_pegawai, "Data Pegawai Gagal Diperbarui",Toast.LENGTH_SHORT).show()
        }
    }

    fun cekValidasi(){
        val nama = et_namalengkap_pegawai.text.toString()
        val alamat = et_alamat_pegawai.text.toString()
        val nohp = et_nohp_pegawai.text.toString()
        val terdaftar = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault()).format(Date())
        val cabang = et_namacabang_pegawai.text.toString()
        val data = modelpegawai(nama, nohp, alamat, cabang)

        //validasi data
            if (nama.isEmpty()) {
                et_namalengkap_pegawai.error = getString(R.string.validasi_nama_pegawai)
                Toast.makeText(this, getString(R.string.validasi_nama_pegawai), Toast.LENGTH_SHORT).show()
                et_namalengkap_pegawai.requestFocus()
                return
            }
            if (alamat.isEmpty()) {
                et_alamat_pegawai.error = getString(R.string.validasi_alamat_pegawai)
                Toast.makeText(this, getString(R.string.validasi_alamat_pegawai), Toast.LENGTH_SHORT).show()
                et_alamat_pegawai.requestFocus()
                return
            }
             if (!nohp.matches(Regex("^[0-9]+$"))) {
                 et_nohp_pegawai.error = "Nomor HP harus berupa angka"
                 Toast.makeText(this, "Nomor HP harus berupa angka", Toast.LENGTH_SHORT).show()
                 et_nohp_pegawai.requestFocus()
                return
            }
            if (cabang.isEmpty()) {
                et_namacabang_pegawai.error = getString(R.string.validasi_namacabang_pegawai)
                Toast.makeText(this, getString(R.string.validasi_namacabang_pegawai), Toast.LENGTH_SHORT).show()
                et_namacabang_pegawai.requestFocus()
                return
        }
            if(bt_simpan_pegawai.text.equals("Simpan")){
                simpan(terdaftar)
            }else if (bt_simpan_pegawai.text.equals("Edit")){
                hidup()
                et_namalengkap_pegawai.requestFocus()
                bt_simpan_pegawai.text="Perbarui"
            }else if (bt_simpan_pegawai.text.equals("Perbarui")){
                update()
            }
    }


    fun simpan(terdaftar: String) {
        val pegawaiBaru = myRef.push()
        val pegawaiId = pegawaiBaru.key ?: return
        val data = modelpegawai(
            id_pegawai = pegawaiId,
            namapegawai = et_namalengkap_pegawai.text.toString(),
            alamatpegawai = et_alamat_pegawai.text.toString(),
            nohppegawai = et_nohp_pegawai.text.toString(),
            terdaftarpegawai = terdaftar,
            cabangpegawai = et_namacabang_pegawai.text.toString()
        )

        pegawaiBaru.setValue(data)
            .addOnSuccessListener {
                Toast.makeText(this, "Pegawai berhasil disimpan", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener{
                Toast.makeText(this, "Gagal menyimpan pegawai", Toast.LENGTH_SHORT).show()
            }
    }

}