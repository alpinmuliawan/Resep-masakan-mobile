package com.alfin.resepmasakan

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class halaman_profile : AppCompatActivity() {
    private val database = FirebaseDatabase.getInstance("https://resepmasakan-2-default-rtdb.asia-southeast1.firebasedatabase.app/")
    private val myRef = database.getReference("users")

    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var btnUpdatePassword: Button
    private lateinit var btnDeleteAccount: Button
    private var accountId: String? = null // ID akun dinamis

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_halaman_profile)

        // Hubungkan elemen UI
        editTextEmail = findViewById(R.id.etEmail)
        editTextPassword = findViewById(R.id.etPassword)
        btnUpdatePassword = findViewById(R.id.btnUpdatePassword)
        btnDeleteAccount = findViewById(R.id.btnDeleteAccount)

        // Memuat data akun dari Firebase
        loadAccountData()

        // Set listener untuk tombol update
        btnUpdatePassword.setOnClickListener {
            updateAccount()
        }

        // Set listener untuk tombol delete
        btnDeleteAccount.setOnClickListener {
            showDeleteConfirmationDialog()
        }
    }

    // Fungsi untuk memuat data akun
    private fun loadAccountData() {
        accountId = "2" // Ganti ini dengan ID pengguna yang valid atau dapatkan dari proses login

        accountId?.let {
            myRef.child(it).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        // Ambil email dan password dari Firebase
                        val email = snapshot.child("email").getValue(String::class.java)
                        val password = snapshot.child("password").getValue(String::class.java)

                        // Set nilai ke EditTexts
                        editTextEmail.setText(email ?: "")
                        editTextPassword.setText(password ?: "")
                    } else {
                        Toast.makeText(
                            this@halaman_profile,
                            "Data akun tidak ditemukan",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@halaman_profile,
                        "Gagal mengambil data: ${error.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }

    // Fungsi untuk update email dan password
    private fun updateAccount() {
        val newEmail = editTextEmail.text.toString().trim()
        val newPassword = editTextPassword.text.toString().trim()

        if (newEmail.isNotEmpty() && newPassword.isNotEmpty() && accountId != null) {
            val updates = mapOf(
                "email" to newEmail,
                "password" to newPassword
            )
            myRef.child(accountId!!).updateChildren(updates)
                .addOnSuccessListener {
                    Toast.makeText(this, "Akun berhasil diperbarui", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Gagal memperbarui akun", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Email atau password tidak boleh kosong", Toast.LENGTH_SHORT).show()
        }
    }

    // Menampilkan dialog konfirmasi sebelum menghapus akun
    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(this).apply {
            setTitle("Konfirmasi Hapus Akun")
            setMessage("Apakah Anda yakin ingin menghapus akun ini?")
            setPositiveButton("Ya") { _, _ -> deleteAccount() }
            setNegativeButton("Tidak") { dialog, _ -> dialog.dismiss() }
            show()
        }
    }

    // Fungsi untuk menghapus akun
    private fun deleteAccount() {
        if (accountId != null) {
            myRef.child(accountId!!).removeValue()
                .addOnSuccessListener {
                    Toast.makeText(this, "Akun berhasil dihapus dari database", Toast.LENGTH_SHORT).show()

                    // Kembali ke halaman login setelah akun dihapus
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Terjadi kesalahan saat menghapus akun", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "ID akun tidak valid", Toast.LENGTH_SHORT).show()
        }
    }
}
