package com.example.laundry

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.laundry.adapter.adapter_pilihpelanggan
import com.example.laundry.modeldata.modelpelanggan
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Activity_pilihpelanggan : AppCompatActivity() {
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("pelanggan")
    lateinit var rvPilihPelanggan: RecyclerView
    lateinit var pelangganList: ArrayList<modelpelanggan>
    lateinit var tvkosong: TextView
    lateinit var progressBar: ProgressBar
    private lateinit var searchView: androidx.appcompat.widget.SearchView
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pilihpelanggan)

        init()

        sharedPref = getSharedPreferences("user_data", MODE_PRIVATE)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        rvPilihPelanggan.layoutManager=layoutManager
        rvPilihPelanggan.setHasFixedSize(true)
        pelangganList = arrayListOf()
        getData()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val adapter = rvPilihPelanggan.adapter
                if (adapter is adapter_pilihpelanggan) {
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
        rvPilihPelanggan = findViewById(R.id.rvTRANSAKSI_pelanggan)
        searchView = findViewById(R.id.searchView_pelanggan)
        tvkosong = findViewById(R.id.tv_kosong)
        progressBar = findViewById(R.id.progressBar)

    }

    fun getData() {
        progressBar.visibility = View.VISIBLE
        tvkosong.visibility = View.GONE

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                pelangganList.clear()

                if (snapshot.exists() && snapshot.hasChildren()) {
                    for (dataSnapshot in snapshot.children) {
                        val pelanggan = dataSnapshot.getValue(modelpelanggan::class.java)
                        if (pelanggan != null) {
                            pelangganList.add(pelanggan)
                        }
                    }

                    if (pelangganList.isNotEmpty()) {
                        tvkosong.visibility = View.GONE
                    } else {
                        tvkosong.visibility = View.VISIBLE
                    }

                    val adapter = adapter_pilihpelanggan(pelangganList)
                    rvPilihPelanggan.adapter = adapter
                } else {
                    tvkosong.visibility = View.VISIBLE
                }

                progressBar.visibility = View.GONE
            }

            override fun onCancelled(error: DatabaseError) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@Activity_pilihpelanggan, error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}