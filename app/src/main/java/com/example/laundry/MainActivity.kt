package com.example.laundry

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.laundry.pelanggan.DataPelanggan
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class MainActivity : AppCompatActivity() {
    lateinit var cv_layanan : CardView
    lateinit var cv_pegawai : CardView
    lateinit var cv_pelanggan : CardView
    lateinit var cv_laporan : CardView
    lateinit var cv_transaksi : CardView
    lateinit var cv_akun : CardView
    lateinit var cv_tambahan: CardView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        cv_layanan = findViewById(R.id.cv_layanan)
        cv_pegawai = findViewById(R.id.cv_pegawai)
        cv_pelanggan = findViewById(R.id.cv_pelanggan)
        cv_laporan = findViewById(R.id.cv_laporan)
        cv_transaksi = findViewById(R.id.cv_transaksi)
        cv_akun = findViewById(R.id.cv_akun)
        cv_tambahan = findViewById(R.id.cv_tambahan)

        cv_layanan.setOnClickListener {
            val intent = Intent(this, data_layanan::class.java)
            startActivity(intent)
        }

        cv_pegawai.setOnClickListener{
            val intent = Intent(this, data_pegawai::class.java)
            startActivity(intent)
        }

        cv_pelanggan.setOnClickListener{
            val intent = Intent(this, DataPelanggan::class.java)
            startActivity(intent)
        }

        cv_transaksi.setOnClickListener{
            val intent = Intent(this, Activity_transaksi::class.java)
            startActivity(intent)
        }

        cv_laporan.setOnClickListener{
            val intent = Intent(this, Activity_laporan::class.java)
            startActivity(intent)
        }

        cv_akun.setOnClickListener{
            val intent = Intent(this, activity_login::class.java)
            startActivity(intent)
        }
        cv_tambahan.setOnClickListener{
            val intent = Intent(this, data_tambahan::class.java)
            startActivity(intent)
        }

        val greetingTextView: TextView = findViewById(R.id.greeting)
        greetingTextView.text = getGreetingMessage()

        val dateTextView: TextView = findViewById(R.id.date)
        dateTextView.text = getCurrentDate()

    }

    private fun getGreetingMessage(): String {
        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return when (currentHour) {
            in 5..11 -> "Selamat Pagi, Zenith"
            in 12..14 -> "Selamat Siang, Zenith"
            in 15..17 -> "Selamat Sore, Zenith"
            else -> "Selamat Malam, Zenith"
        }
    }

    private fun getCurrentDate(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
        return dateFormat.format(calendar.time)
    }
}
