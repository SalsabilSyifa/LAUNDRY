package com.example.laundry

import android.app.Activity
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.laundry.R
import com.example.laundry.adapter.adapter_laporan
import com.example.laundry.databinding.ActivityLaporanBinding
import com.example.laundry.modeldata.modelLaporan
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Locale

class Activity_laporan : AppCompatActivity() {

    private lateinit var binding: ActivityLaporanBinding
    private lateinit var database: DatabaseReference
    private lateinit var laporanList: MutableList<modelLaporan>
    private lateinit var laporanAdapter: adapter_laporan


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLaporanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance().getReference("laporan")
        laporanList = mutableListOf()
        laporanAdapter = adapter_laporan(this, laporanList)

        binding.rvLaporan.apply {
            layoutManager = LinearLayoutManager(this@Activity_laporan)
            adapter = laporanAdapter
        }


        ambilDataLaporan()
        updateSemuaStatusJikaKosong()
    }

    private fun ambilDataLaporan() {
        binding.progressBar.visibility = View.VISIBLE
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                laporanList.clear()
                for (data in snapshot.children) {
                    val laporan = data.getValue(modelLaporan::class.java)
                    if (laporan != null) {
                        laporanList.add(laporan)

                    }
                }
                val format = SimpleDateFormat("dd-MM-yyyy  HH:mm:ss", Locale("in", "ID"))

                laporanList.sortByDescending {
                    try {
                        format.parse(it.tanggal) ?: format.parse("01-01-1970 00:00:00")
                    } catch (e: Exception) {
                        Log.e("PARSE_TANGGAL", "Format salah: ${it.tanggal}")
                        format.parse("01-01-1970 00:00:00") // fallback tanggal jika format salah
                    }
                }

                laporanAdapter.notifyDataSetChanged()
                binding.ac.visibility = View.GONE
                binding.progressBar.visibility = View.GONE
            }

            override fun onCancelled(error: DatabaseError) {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this@Activity_laporan, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Opsional: panggil sekali untuk perbaiki data lama
    private fun updateSemuaStatusJikaKosong() {
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children) {
                    val laporan = data.getValue(modelLaporan::class.java)
                    if (laporan != null) {
                        val id = data.key ?: continue
                        if (laporan.status.isNullOrEmpty()) {
                            val statusBaru = if (laporan.metodePembayaran.equals("Bayar Nanti", true)) {
                                "Belum Dibayar"
                            } else {
                                "Sudah Dibayar"
                            }
                            database.child(id).child("status").setValue(statusBaru)
                            Log.d("FIREBASE_DEBUG", "Jumlah data: ${snapshot.childrenCount}")

                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })

    }
}
