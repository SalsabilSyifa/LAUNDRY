package com.example.laundry.pelanggan

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.laundry.R
import com.example.laundry.activity_tambahan_pelanggan
import com.example.laundry.adapter.DataPelangganAdapter
import com.example.laundry.adapter.adapter_data_pelanggan
import com.example.laundry.modeldata.modelpelanggan
import com.example.laundry.tambah_pegawai
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DataPelanggan : AppCompatActivity() {
    lateinit var bt_data_pelanggan_tambah : FloatingActionButton
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("pelanggan")
    lateinit var rv_data_pelanggan: RecyclerView
    lateinit var pelangganList : ArrayList<modelpelanggan>





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_data_pelanggan)

        init()

        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        rv_data_pelanggan.layoutManager = layoutManager
        rv_data_pelanggan.setHasFixedSize(true)
        pelangganList = arrayListOf<modelpelanggan>()

        getData()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        bt_data_pelanggan_tambah = findViewById(R.id.bt_data_pelanggan_tambah)

        bt_data_pelanggan_tambah.setOnClickListener {
            val intent = Intent(this, TambahPelanggan::class.java)
            startActivity(intent)
        }

    }
    fun init (){
        rv_data_pelanggan = findViewById(R.id.rv_data_pelanggan)
        bt_data_pelanggan_tambah = findViewById(R.id.bt_data_pelanggan_tambah)
    }

    fun getData(){
        val query = myRef.orderByChild("idPelanggan").limitToLast(100)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    pelangganList.clear()
                    for (dataSnapshot in snapshot.children) {
                        val pegawai = dataSnapshot.getValue(modelpelanggan::class.java)
                        pegawai?.let { pelangganList.add(it) }
                    }
                    val adapter = adapter_data_pelanggan(pelangganList)
                    rv_data_pelanggan.adapter = adapter
                    adapter.notifyDataSetChanged()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@DataPelanggan, error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }




}