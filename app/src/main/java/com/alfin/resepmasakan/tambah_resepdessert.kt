package com.alfin.resepmasakan

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

class tambah_resepdessert : AppCompatActivity() {

    private lateinit var etRecipeName: EditText
    private lateinit var etRecipeDescription: EditText
    private lateinit var etIngredients: EditText
    private lateinit var etSteps: EditText
    private lateinit var btnSaveRecipe: Button

    private val database = FirebaseDatabase.getInstance("https://resepmasakan-2-default-rtdb.asia-southeast1.firebasedatabase.app/")
    private val recipesRef = database.getReference("Dessert")

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_resepdessert)

        // Inisialisasi View
        etRecipeName = findViewById(R.id.etRecipeName)
        etRecipeDescription = findViewById(R.id.etRecipeDescription)
        etIngredients = findViewById(R.id.etIngredients)
        etSteps = findViewById(R.id.etSteps)
        btnSaveRecipe = findViewById(R.id.btnresepdessertsimpan)

        // Setup Spinner


        // Tombol Simpan
        btnSaveRecipe.setOnClickListener {
            val id = recipesRef.push().key ?: return@setOnClickListener
            val recipe = getRecipeFromInputs(id)
            if (validateInputs(recipe)) {
                recipesRef.child(id).setValue(recipe)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Resep berhasil disimpan", Toast.LENGTH_SHORT).show()
                        clearInputs()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Gagal menyimpan resep", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    private fun validateInputs(recipe: Recipe): Boolean {
        if (recipe.name!!.isEmpty() || recipe.description!!.isEmpty() ||
            recipe.ingredients!!.isEmpty() || recipe.steps!!.isEmpty()
        ) {
            Toast.makeText(this, "Semua bidang harus diisi", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun getRecipeFromInputs(id: String): Recipe {
        val name = etRecipeName.text.toString().trim()
        val description = etRecipeDescription.text.toString().trim()
        val ingredients = etIngredients.text.toString().trim()
        val steps = etSteps.text.toString().trim()

        return Recipe(id, name, description, ingredients, steps,)
    }

    private fun clearInputs() {
        etRecipeName.text.clear()
        etRecipeDescription.text.clear()
        etIngredients.text.clear()
        etSteps.text.clear()
    }
}
