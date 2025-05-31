package com.example.laundry

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.laundry.adapter.adapter_data_cabang
import com.example.laundry.adapter.adapter_data_pelanggan
import com.example.laundry.modeldata.modelcabang
import com.example.laundry.modeldata.modelpelanggan
import com.example.laundry.pelanggan.TambahPelanggan
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class activity_data_cabang : AppCompatActivity() {
    lateinit var bt_data_cabang_tambah: FloatingActionButton
    lateinit var rv_data_cabang: RecyclerView
    lateinit var cabangList: ArrayList<modelcabang>
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("cabang")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_data_cabang)
        init()


        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        rv_data_cabang.layoutManager = layoutManager
        rv_data_cabang.setHasFixedSize(true)
        cabangList = arrayListOf<modelcabang>()

        bt_data_cabang_tambah.setOnClickListener {
            val intent = Intent(this, activity_tambah_cabang::class.java)
            startActivity(intent)
        }
        getData()

        val fabTambahCabang: FloatingActionButton = findViewById(R.id.bt_data_cabang_tambah)
        fabTambahCabang.setOnClickListener {
            val intent = Intent(this, activity_tambah_cabang::class.java)
            intent.putExtra("judul", this.getString(R.string.tambah_cabang))
            intent.putExtra("id", "")
            intent.putExtra("tv_nama_cabang", "")
            intent.putExtra("tv_alamat_cabang", "")
            intent.putExtra("tv_layanan_cabang", "")
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    fun init() {
        rv_data_cabang = findViewById(R.id.rv_data_cabang)
        bt_data_cabang_tambah = findViewById(R.id.bt_data_cabang_tambah)
    }

    fun getData() {
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                cabangList.clear()

                if (snapshot.exists()) {
                    for (cabangSnap in snapshot.children) {
                        val cabang = cabangSnap.getValue(modelcabang::class.java)
                        if (cabang != null) {
                            cabangList.add(cabang)
                            Log.d("FirebaseData", "Data cabang: ${cabang.tv_nama_cabang}")

                        }
                    }

                    rv_data_cabang.adapter = adapter_data_cabang(cabangList)
                } else {
                    Toast.makeText(this@activity_data_cabang, "Data kosong", Toast.LENGTH_SHORT).show()
                    Log.d("FirebaseData", "Data tidak ditemukan")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@activity_data_cabang,
                    "Gagal memuat data: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}