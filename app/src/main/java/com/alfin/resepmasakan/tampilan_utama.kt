package com.alfin.resepmasakan

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class tampilan_utama : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tampilan_utama)

        // Tombol Appetizer
        val appetizerButton: Button = findViewById(R.id.appetizer)
        appetizerButton.setOnClickListener {
            val intent = Intent(this, resep_appetizer::class.java)
            startActivity(intent)
        }

        // Tombol Main Course
        val mainCourseButton: Button = findViewById(R.id.maincourse)
        mainCourseButton.setOnClickListener {
            val intent = Intent(this, resep_maincourse::class.java)
            startActivity(intent)
            // Toast.makeText(this, "Main Course Button Clicked", Toast.LENGTH_SHORT).show()
        }

        // Tombol Dessert
        val dessertButton: Button = findViewById(R.id.dessert1)
        dessertButton.setOnClickListener {
            val intent = Intent(this, resep_dessert::class.java)
            startActivity(intent)
            // Toast.makeText(this, "Dessert Button Clicked", Toast.LENGTH_SHORT).show()
        }

        // Tombol Account Info
        val accountInfoButton: ImageButton = findViewById(R.id.account_info_button)
        accountInfoButton.setOnClickListener {
            // Contoh Intent ke halaman profil pengguna
            val intent = Intent(this, halaman_profile::class.java)
            startActivity(intent)
            // Toast.makeText(this, "Account Info Button Clicked", Toast.LENGTH_SHORT).show()
        }
    }
}
