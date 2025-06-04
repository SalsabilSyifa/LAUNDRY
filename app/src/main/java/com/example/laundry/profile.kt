package com.example.laundry

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class profile : AppCompatActivity() {
    private lateinit var etUsername: EditText
    private lateinit var etNoHp: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnSimpan: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)
        etUsername = findViewById(R.id.et_username)
        etNoHp = findViewById(R.id.et_nohp)
        etPassword = findViewById(R.id.et_password)
        btnSimpan = findViewById(R.id.btn_update)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val nohp = getSharedPreferences("users", MODE_PRIVATE).getString("nohp", "") ?: ""

        // Ambil data dari Firebase
        val ref = FirebaseDatabase.getInstance().getReference("users").child(nohp)
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                etUsername.setText(snapshot.child("username").getValue(String::class.java))
                etNoHp.setText(nohp)
                etPassword.setText(snapshot.child("password").getValue(String::class.java))
            }

            override fun onCancelled(error: DatabaseError) {}
        })

        btnSimpan.setOnClickListener {
            val newUsername = etUsername.text.toString()
            val newNoHp = etNoHp.text.toString()
            val newPassword = etPassword.text.toString()

            val userMap = mapOf(
                "username" to newUsername,
                "password" to newPassword
            )

            // Update Firebase
            FirebaseDatabase.getInstance().getReference("users")
                .child(newNoHp)
                .setValue(userMap)
                .addOnSuccessListener {
                    // Simpan username baru ke SharedPreferences
                    val prefs = getSharedPreferences("users", MODE_PRIVATE)
                    prefs.edit().putString("username", newUsername).apply()

                    Toast.makeText(this, "Berhasil diperbarui", Toast.LENGTH_SHORT).show()
                    finish()
                }
        }

    }

}
