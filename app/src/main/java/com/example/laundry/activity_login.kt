package com.example.laundry

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.MotionEvent
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class activity_login : AppCompatActivity() {
    private lateinit var etNoHp: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnMasuk: Button
    private lateinit var tvRegister: TextView
    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        etNoHp = findViewById(R.id.et_nohp)
        etPassword = findViewById(R.id.et_password)
        btnMasuk = findViewById(R.id.bt_Masuk)
        tvRegister = findViewById(R.id.tv_register)

        // Toggle password dengan klik ikon di dalam EditText
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

        btnMasuk.setOnClickListener {
            val nohp = etNoHp.text.toString()
            val password = etPassword.text.toString()

            if (nohp.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, getString(R.string.isidulu), Toast.LENGTH_SHORT).show()
            } else {
                loginUser(nohp, password)
            }
        }

        tvRegister.setOnClickListener {
            startActivity(Intent(this, register::class.java))
        }
    }

    private fun togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible

        val iconRes = if (isPasswordVisible) R.drawable.ic_visibility else R.drawable.ic_visibility_off

        // Load drawable dan atur ukuran secara manual
        val drawable = ContextCompat.getDrawable(this, iconRes)
        drawable?.setBounds(0, 0, 48, 48) // Ubah ukuran di sini (width x height dalam pixel)

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


    private fun loginUser(nohp: String, password: String) {
        val ref = FirebaseDatabase.getInstance().getReference("users")

        ref.child(nohp).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val savedPassword = snapshot.child("password").getValue(String::class.java)
                    val usernameDb = snapshot.child("username").getValue(String::class.java)
                    if (savedPassword == password) {
                        val prefs = getSharedPreferences("users", MODE_PRIVATE)

                        prefs.edit().apply {
                            putString("nohp", nohp)
                            putString("username", usernameDb) // <== Tambahan penting
                            apply()
                        }


                        startActivity(Intent(this@activity_login, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this@activity_login, "Password salah", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@activity_login, "User tidak ditemukan", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@activity_login, "Database error", Toast.LENGTH_SHORT).show()
            }
        })
    }

}
