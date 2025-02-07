package com.example.laundry

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.laundry.adapter.adapter_data_pegawai
import com.example.laundry.modeldata.modelpegawai
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class data_pegawai : AppCompatActivity() {
    lateinit var bt_data_pegawai_tambah : FloatingActionButton
    lateinit var rv_data_pegawai: RecyclerView
    lateinit var pegawaiList : ArrayList<modelpegawai>
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("pegawai")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_data_pegawai)

        init()

        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        rv_data_pegawai.layoutManager = layoutManager
        rv_data_pegawai.setHasFixedSize(true)
        pegawaiList = arrayListOf<modelpegawai>()

        getData()
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        bt_data_pegawai_tambah = findViewById(R.id.bt_data_pegawai_tambah)

        bt_data_pegawai_tambah.setOnClickListener {
            val intent = Intent(this, tambah_pegawai::class.java)
            startActivity(intent)
        }
    }
    
    fun init() {
        rv_data_pegawai = findViewById(R.id.rv_data_pegawai)
        bt_data_pegawai_tambah = findViewById(R.id.bt_data_pegawai_tambah)
        
    }
    
    
    fun getData() {
        val query = myRef.orderByChild("idPegawai").limitToLast(100)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    pegawaiList.clear()
                    for (dataSnapshot in snapshot.children) {
                        val pegawai = dataSnapshot.getValue(modelpegawai::class.java)
                        pegawai?.let{ pegawaiList.add(it) }
                    }
                    val adapter = adapter_data_pegawai(pegawaiList)
                    rv_data_pegawai.adapter = adapter
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) { }
        })
    }
}