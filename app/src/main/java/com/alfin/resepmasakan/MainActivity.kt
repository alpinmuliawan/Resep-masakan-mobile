package com.alfin.resepmasakan

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var database: DatabaseReference

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextEmail = findViewById(R.id.emailuser)
        editTextPassword = findViewById(R.id.sandiuser)
        database =
            FirebaseDatabase.getInstance("https://resepmasakan-2-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("users")

        val buttonLogin: Button = findViewById(R.id.buttonlogin)
        buttonLogin.setOnClickListener {
            loginUser()
        }

        val buttonBuatAkun: Button = findViewById(R.id.buttonbuatakun)
        buttonBuatAkun.setOnClickListener {
            val intent = Intent(this, pembuatan_akun::class.java)
            startActivity(intent)
        }
    }

    private fun loginUser() {
        val email = editTextEmail.text.toString().trim()
        val password = editTextPassword.text.toString().trim()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            database.orderByChild("email").equalTo(email)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            var loginSuccessful = false
                            for (userSnapshot in snapshot.children) {
                                val userPassword =
                                    userSnapshot.child("password").getValue(String::class.java)
                                if (userPassword == password) {
                                    loginSuccessful = true
                                    // Redirect to the main activity if login is successful
                                    val intent =
                                        Intent(this@MainActivity, tampilan_utama::class.java)
                                    startActivity(intent)
                                    finish() // Close MainActivity after login
                                    return
                                }
                            }
                            if (!loginSuccessful) {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Password salah",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            Toast.makeText(
                                this@MainActivity,
                                "Email tidak ditemukan",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(
                            this@MainActivity,
                            "Terjadi kesalahan: ${error.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        } else {
            Toast.makeText(this, "Email dan password tidak boleh kosong", Toast.LENGTH_SHORT).show()
        }
    }
}