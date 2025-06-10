package com.example.laundry.pelanggan
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.laundry.R
import com.example.laundry.adapter.adapter_data_pelanggan
import com.example.laundry.modeldata.modelpelanggan
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DataPelanggan : AppCompatActivity() {
    lateinit var bt_data_pelanggan_tambah: FloatingActionButton
    lateinit var rv_data_pelanggan: RecyclerView
    lateinit var pelangganList: ArrayList<modelpelanggan>
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("pelanggan")

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

        bt_data_pelanggan_tambah.setOnClickListener {
            val intent = Intent(this, TambahPelanggan::class.java)
            startActivity(intent)
        }
        getData()

        bt_data_pelanggan_tambah.setOnClickListener {
            val intent = Intent(this, TambahPelanggan::class.java)
            intent.putExtra("judul", this.getString(R.string.tambah_pelanggan))
            intent.putExtra("id", "")
            intent.putExtra("nama", "")
            intent.putExtra("alamat", "")
            intent.putExtra("nohp", "")
            startActivity(intent)
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun init() {
        rv_data_pelanggan = findViewById(R.id.rv_data_pelanggan)
        bt_data_pelanggan_tambah = findViewById(R.id.bt_data_pelanggan_tambah)

    }

    fun getData() {
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                pelangganList.clear()

                if (snapshot.exists()) {
                    for (pelangganSnap in snapshot.children) {
                        val pelanggan = pelangganSnap.getValue(modelpelanggan::class.java)
                        if (pelanggan != null) {
                            pelangganList.add(pelanggan)
                            Log.d("FirebaseData", "Data pelanggan: ${pelanggan.nama}")

                        }
                    }
                    val adapter = adapter_data_pelanggan(pelangganList)
                    rv_data_pelanggan.adapter = adapter

                    adapter.setOnItemClickListener(object : adapter_data_pelanggan.OnItemClickListener {
                        override fun onDeleteClick(pelanggan: modelpelanggan) {
                            AlertDialog.Builder(this@DataPelanggan)
                                .setTitle("Hapus Data")
                                .setMessage("Yakin ingin menghapus ${pelanggan.nama}?")
                                .setPositiveButton("Ya") { _, _ ->
                                    // Hapus dari Firebase
                                    FirebaseDatabase.getInstance().getReference("pelanggan")
                                        .child(pelanggan.id_pelanggan!!)
                                        .removeValue()
                                        .addOnSuccessListener {
                                            Toast.makeText(this@DataPelanggan, getString(R.string.berhasildihapus), Toast.LENGTH_SHORT).show()
                                        }
                                        .addOnFailureListener {
                                            Toast.makeText(this@DataPelanggan, "Gagal menghapus data", Toast.LENGTH_SHORT).show()
                                        }
                                }
                                .setNegativeButton("Batal", null)
                                .show()
                        }
                    })
                } else {
                    Toast.makeText(this@DataPelanggan, "Data kosong", Toast.LENGTH_SHORT).show()
                    Log.d("FirebaseData", "Data tidak ditemukan")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@DataPelanggan,
                    "Gagal memuat data: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }



}
