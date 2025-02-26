package com.example.laundry

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.laundry.adapter.adapter_data_layanan
import com.example.laundry.adapter.adapter_data_pelanggan
import com.example.laundry.modeldata.modellayanan
import com.example.laundry.modeldata.modelpelanggan
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class data_layanan : AppCompatActivity() {
    lateinit var bt_data_layanan_tambah : FloatingActionButton
    lateinit var rv_data_layanan: RecyclerView
    lateinit var layananList: ArrayList<modellayanan>
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("layanan")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.data_layanan)

        init()

        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        rv_data_layanan.layoutManager = layoutManager
        rv_data_layanan.setHasFixedSize(true)
        layananList = arrayListOf<modellayanan> ()

        getData()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        bt_data_layanan_tambah = findViewById(R.id.bt_data_layanan_tambah)

        bt_data_layanan_tambah.setOnClickListener {
            val intent = Intent(this, tambah_layanan::class.java)
            startActivity(intent)
        }
    }

    fun init(){
        rv_data_layanan = findViewById(R.id.rv_data_layanan)
        bt_data_layanan_tambah = findViewById(R.id.bt_data_layanan_tambah)
    }

    fun getData(){
        val query = myRef.orderByChild("idLayanan").limitToLast(100)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    layananList.clear()
                    for (dataSnapshot in snapshot.children) {
                        val pelanggan = dataSnapshot.getValue(modellayanan::class.java)
                        pelanggan?.let { layananList.add(it) }
                    }
                    val adapter = adapter_data_layanan(layananList)
                    rv_data_layanan.adapter = adapter
                    adapter.notifyDataSetChanged()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@data_layanan, error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}