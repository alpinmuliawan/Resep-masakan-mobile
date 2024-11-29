package com.alfin.resepmasakan

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class resep_maincourse : AppCompatActivity() {

    private val database =
        FirebaseDatabase.getInstance("https://resepmasakan-2-default-rtdb.asia-southeast1.firebasedatabase.app/")
    private val recipesRef = database.getReference("Maincourse")

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdapterMain
    private val resepList = mutableListOf<Recipe>()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resep_maincourse)

        try {
            // Back Button
            val backButton: ImageButton = findViewById(R.id.backButton)
            backButton.setOnClickListener { finish() }

            // Add Recipe Button
            val tambahResepButton: ImageButton = findViewById(R.id.buttontambahresep2)
            tambahResepButton.setOnClickListener {
                val intent = Intent(this,tambah_resepmaincoures::class.java)
                startActivity(intent)
            }

            // Setup RecyclerView
            recyclerView = findViewById(R.id.recyclerView)
            recyclerView.layoutManager = LinearLayoutManager(this)

            adapter = AdapterMain(this, resepList)
            recyclerView.adapter = adapter

            // Load data dari Firebase
            loadDataFromFirebase()
        } catch (e: Exception) {
            Toast.makeText(this, "Terjadi kesalahan: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadDataFromFirebase() {
        recipesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    resepList.clear()
                    for (data in snapshot.children) {
                        val recipe = data.getValue(Recipe::class.java)
                        if (recipe != null) {
                            resepList.add(recipe)
                        }
                    }
                    adapter.notifyDataSetChanged()
                } catch (e: Exception) {
                    Toast.makeText(this@resep_maincourse, "Error parsing data: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@resep_maincourse, "Gagal mengambil data: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
