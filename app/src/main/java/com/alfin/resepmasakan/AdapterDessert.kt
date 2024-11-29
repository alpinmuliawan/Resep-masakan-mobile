package com.alfin.resepmasakan

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase

class AdapterDessert(
    private val context: Context,
    private val resepList: MutableList<Recipe> // MutableList untuk manipulasi daftar
) : RecyclerView.Adapter<AdapterDessert.ResepViewHolder>() {

    // Referensi ke Firebase Database
    private val database = FirebaseDatabase.getInstance("https://resepmasakan-2-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Dessert")

    // ViewHolder untuk item
    class ResepViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val judul: TextView = view.findViewById(R.id.judul1)
        val tombol: Button = view.findViewById(R.id.tombol1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResepViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_resepdessert, parent, false)
        return ResepViewHolder(view)
    }

    override fun onBindViewHolder(holder: ResepViewHolder, position: Int) {
        val resep = resepList[position]

        // Tampilkan judul resep
        holder.judul.text = resep.name

        // Tombol untuk melihat detail resep
        holder.tombol.setOnClickListener {
            showRecipeDialog(resep)
        }
    }

    override fun getItemCount(): Int = resepList.size

    // Menampilkan dialog detail resep
    private fun showRecipeDialog(resep: Recipe) {
        // Inflate layout dialog
        val dialogView =
            LayoutInflater.from(context).inflate(R.layout.activity_tampilan_resep, null)

        // Setup views
        val tvJudulResep = dialogView.findViewById<TextView>(R.id.judulresep)
        val tvDeskripsiResep = dialogView.findViewById<TextView>(R.id.deskripsiresep)
        val tvBahanResep = dialogView.findViewById<TextView>(R.id.bahanresep)
        val tvLangkahResep = dialogView.findViewById<TextView>(R.id.stepsresep)
        val btnCloseDialog = dialogView.findViewById<Button>(R.id.tutupresep)
        val btnHapusResep = dialogView.findViewById<Button>(R.id.hapusresep)

        // Set data
        tvJudulResep.text = resep.name
        tvDeskripsiResep.text = resep.description
        tvBahanResep.text = resep.ingredients
        tvLangkahResep.text = resep.steps

        // Buat dialog
        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .setCancelable(true)
            .create()

        // Tombol tutup dialog
        btnCloseDialog.setOnClickListener {
            dialog.dismiss()
        }

        // Tombol hapus resep
        btnHapusResep.setOnClickListener {
            dialog.dismiss()
            deleteRecipe(resep)
        }

        // Tampilkan dialog
        dialog.show()
    }

    // Fungsi untuk menghapus resep dari Firebase dan daftar lokal
    private fun deleteRecipe(resep: Recipe) {
        if (resep.id != null) {
            database.child(resep.id).removeValue()
                .addOnSuccessListener {
                    // Hapus dari daftar lokal
                    resepList.remove(resep)
                    notifyDataSetChanged()
                    Toast.makeText(context, "Resep '${resep.name}' berhasil dihapus!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "Gagal menghapus resep: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(context, "Resep tidak memiliki ID yang valid untuk dihapus.", Toast.LENGTH_SHORT).show()
        }
    }
}
