package com.alfin.resepmasakan

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

class resep_appetizer : AppCompatActivity() {

    private val database =
        FirebaseDatabase.getInstance("https://resepmasakan-2-default-rtdb.asia-southeast1.firebasedatabase.app/")
    private val recipesRef = database.getReference("Appetizer")

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdapterAppetizer
    private val resepList = mutableListOf<Recipe>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resep_apptizer)

        // Back Button
        val backButton: ImageButton = findViewById(R.id.backButton)
        backButton.setOnClickListener { finish() }

        // Add Recipe Button
        val tambahResepButton: ImageButton = findViewById(R.id.tambahresep)
        tambahResepButton.setOnClickListener {
            val intent = Intent(this,Tambah_resepappetizer::class.java)
            startActivity(intent)
        }

        // Setup RecyclerView
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = AdapterAppetizer(this, resepList)
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
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@resep_appetizer, "Gagal mengambil data: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
