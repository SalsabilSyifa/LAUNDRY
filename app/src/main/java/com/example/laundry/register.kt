package com.example.laundry

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.MotionEvent
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.FirebaseDatabase

class register : AppCompatActivity() {
    private var isPasswordVisible = false
    private lateinit var etPassword: EditText  // Dideklarasikan di luar agar bisa diakses fungsi lain

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        val etNama = findViewById<EditText>(R.id.et_username)
        val etNoHp = findViewById<EditText>(R.id.et_nohp)
        etPassword = findViewById(R.id.et_password)
        val btnRegister = findViewById<Button>(R.id.bt_daftar)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        etPassword.setOnTouchListener { _, event ->
            val drawableEnd = etPassword.compoundDrawablesRelative[2]
            if (drawableEnd != null && event.action == MotionEvent.ACTION_UP) {
                val drawableWidth = drawableEnd.bounds.width()
                val touchX = event.rawX
                val drawableStartX = etPassword.right - etPassword.paddingEnd - drawableWidth

                if (touchX >= drawableStartX) {
                    togglePasswordVisibility()
                    return@setOnTouchListener true
                }
            }
            false
        }

        btnRegister.setOnClickListener {
            val nama = etNama.text.toString()
            val nohp = etNoHp.text.toString()
            val password = etPassword.text.toString()

            if (nama.isEmpty() || nohp.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Isi semua field", Toast.LENGTH_SHORT).show()
            } else {
                val user = mapOf(
                    "username" to nama,
                    "password" to password
                )

                FirebaseDatabase.getInstance().getReference("users")
                    .child(nohp)
                    .setValue(user)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Registrasi berhasil", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, activity_login::class.java))
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Gagal daftar", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    private fun togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible

        val iconRes = if (isPasswordVisible) R.drawable.ic_visibility else R.drawable.ic_visibility_off
        val drawable = ContextCompat.getDrawable(this, iconRes)
        drawable?.setBounds(0, 0, 48, 48) // Sesuaikan ukuran ikon

        etPassword.setCompoundDrawablesRelative(
            null, null,
            drawable,
            null
        )

        etPassword.inputType = if (isPasswordVisible)
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        else
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

        etPassword.setSelection(etPassword.text.length)
    }
}
