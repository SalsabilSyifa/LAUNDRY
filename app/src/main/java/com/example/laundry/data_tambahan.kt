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
import com.example.laundry.adapter.adapter_data_tambahan
import com.example.laundry.modeldata.modellayanan
import com.example.laundry.modeldata.modeltambahan
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class data_tambahan : AppCompatActivity() {
    lateinit var bt_data_tambahan : FloatingActionButton
    lateinit var rv_data_tambahan: RecyclerView
    lateinit var tambahanList: ArrayList<modeltambahan>
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("tambahan")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_data_tambahan)
        init()
        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        rv_data_tambahan.layoutManager = layoutManager
        rv_data_tambahan.setHasFixedSize(true)
        tambahanList = arrayListOf<modeltambahan> ()

        getData()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        bt_data_tambahan = findViewById(R.id.bt_data_tambahan_tambah)

        bt_data_tambahan.setOnClickListener {
            val intent = Intent(this, activity_tambah_tambahan::class.java)
            startActivity(intent)
        }
    }

    fun init(){
        rv_data_tambahan = findViewById(R.id.rv_data_tambahan)
        bt_data_tambahan = findViewById(R.id.bt_data_tambahan_tambah)
    }

    fun getData(){
        val query = myRef.orderByChild("idTambahan").limitToLast(100)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    tambahanList.clear()
                    for (dataSnapshot in snapshot.children) {
                        val tambahan = dataSnapshot.getValue(modeltambahan::class.java)
                        tambahan?.let { tambahanList.add(it) }
                    }
                    val adapter = adapter_data_tambahan(tambahanList)
                    rv_data_tambahan.adapter = adapter
                    adapter.notifyDataSetChanged()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@data_tambahan, error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}