package com.example.laundry

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.laundry.adapter.adapter_pilihlayanan
import com.example.laundry.adapter.adapter_pilihpelanggan
import com.example.laundry.modeldata.modellayanan
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Activity_pilihlayanan : AppCompatActivity() {
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("layanan")
    lateinit var rvPilihLayanan: RecyclerView
    lateinit var layananList: ArrayList<modellayanan>
    lateinit var tvkosong: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var searchView: androidx.appcompat.widget.SearchView
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pilihlayanan)

        init()

        sharedPref = getSharedPreferences("user_data", MODE_PRIVATE)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        rvPilihLayanan.layoutManager=layoutManager
        rvPilihLayanan.setHasFixedSize(true)
        layananList = arrayListOf()
        getData()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val adapter = rvPilihLayanan.adapter
                if (adapter is adapter_pilihlayanan) {
                    adapter.filter.filter(newText)
                }
                return true
            }
        })

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun init() {
        rvPilihLayanan = findViewById(R.id.rvTRANSAKSI_Layanan)
        searchView = findViewById(R.id.searchView_layanan)
        tvkosong = findViewById(R.id.tv_kosong)
        progressBar = findViewById(R.id.progressBar)

    }

    fun getData() {
        progressBar.visibility = View.VISIBLE
        tvkosong.visibility = View.GONE  // Sembunyikan teks kosong saat mulai loading

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                layananList.clear()

                if (snapshot.exists() && snapshot.hasChildren()) {
                    for (dataSnapshot in snapshot.children) {
                        val layanan = dataSnapshot.getValue(modellayanan::class.java)
                        if (layanan != null) {
                            layananList.add(layanan)
                        }
                    }

                    if (layananList.isNotEmpty()) {
                        tvkosong.visibility = View.GONE
                    } else {
                        tvkosong.visibility = View.VISIBLE
                    }

                    rvPilihLayanan.adapter = adapter_pilihlayanan(layananList)

                } else {
                    // Tidak ada node 'layanan' atau kosong
                    tvkosong.visibility = View.VISIBLE
                }

                progressBar.visibility = View.GONE
            }

            override fun onCancelled(error: DatabaseError) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@Activity_pilihlayanan, error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

}