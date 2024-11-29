package com.alfin.resepmasakan

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class pembuatan_akun : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var btnSignUp: Button
    private lateinit var tvLoginLink: TextView
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pembuatan_akun)  // Ganti dengan nama layout yang sesuai

        // Inisialisasi FirebaseDatabase
        database = FirebaseDatabase.getInstance("https://resepmasakan-2-default-rtdb.asia-southeast1.firebasedatabase.app/")

        // Inisialisasi view
        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        btnSignUp = findViewById(R.id.btnSignUp)
        tvLoginLink = findViewById(R.id.btkehalamanlogin)  // Pastikan ID sesuai dengan XML

        // Tombol Buat Akun\
        btnSignUp.setOnClickListener {
            val name = etName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val confirmPassword = etConfirmPassword.text.toString().trim()

            // Validasi input
            if (name.isEmpty()) {
                etName.error = "Nama lengkap harus diisi"
                return@setOnClickListener
            }
            if (email.isEmpty()) {
                etEmail.error = "Email harus diisi"
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                etPassword.error = "Kata sandi harus diisi"
                return@setOnClickListener
            }
            if (confirmPassword.isEmpty()) {
                etConfirmPassword.error = "Konfirmasi kata sandi harus diisi"
                return@setOnClickListener
            }
            if (password != confirmPassword) {
                etConfirmPassword.error = "Kata sandi dan konfirmasi tidak cocok"
                return@setOnClickListener
            }

            // Menyimpan data pengguna dengan ID berurutan
            val userRef = database.reference.child("users")
            userRef.orderByKey().limitToLast(1).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    // Tentukan ID terakhir dan tambahkan 1 untuk ID baru
                    val lastId = snapshot.children.firstOrNull()?.key?.toIntOrNull() ?: 0
                    val newId = (lastId + 1).toString()

                    val userData = mapOf(
                        "name" to name,
                        "email" to email,
                        "password" to password
                    )

                    // Simpan data pengguna baru dengan ID baru
                    userRef.child(newId).setValue(userData)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this@pembuatan_akun, "Akun berhasil dibuat!", Toast.LENGTH_SHORT).show()
                                // Redirect ke halaman login atau halaman utama
                                finish() // Ganti dengan intent ke halaman login
                            } else {
                                Toast.makeText(this@pembuatan_akun, "Pendaftaran gagal: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                            }
                        }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@pembuatan_akun, "Gagal mengambil ID terakhir", Toast.LENGTH_SHORT).show()
                }
            })
        }

        // Tombol untuk berpindah ke halaman login
        tvLoginLink.setOnClickListener {
            // Pindah ke activity login
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
