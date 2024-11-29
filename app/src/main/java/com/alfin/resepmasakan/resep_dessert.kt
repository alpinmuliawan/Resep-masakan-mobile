package com.alfin.resepmasakan

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class resep_dessert : AppCompatActivity() {

    private val database =
        FirebaseDatabase.getInstance("https://resepmasakan-2-default-rtdb.asia-southeast1.firebasedatabase.app/")
    private val recipesRef = database.getReference("Dessert")

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdapterDessert
    private val resepList = mutableListOf<Recipe>()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resep_dessert)

        // Back Button
        val backButton: ImageButton = findViewById(R.id.buttonkeluar)
        backButton.setOnClickListener { finish() }

        // Add Recipe Button
        findViewById<ImageButton>(R.id.buttontambahresep1)?.setOnClickListener {
            startActivity(Intent(this, tambah_resepdessert::class.java))
        }

        // Setup RecyclerView
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = AdapterDessert(this, resepList)
        recyclerView.adapter = adapter

        // Load data dari Firebase
        loadDataFromFirebase()
    }

    private fun loadDataFromFirebase() {
        recipesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                resepList.clear()
                for (data in snapshot.children) {
                    val recipe = data.getValue(Recipe::class.java)
                    if (recipe != null) {
                        resepList.add(recipe)
                        Log.d("FirebaseData", "Recipe added: ${recipe.name}")
                    }
                }
                adapter.notifyDataSetChanged()
                Log.d("FirebaseData", "Total recipes: ${resepList.size}")
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@resep_dessert,
                    "Gagal mengambil data: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}
