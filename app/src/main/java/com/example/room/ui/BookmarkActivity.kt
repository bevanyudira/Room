package com.example.room.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.room.database.BookmarkRoomDatabase
import com.example.room.databinding.ActivityBookmarkBinding
import model.Quote

class BookmarkActivity : AppCompatActivity() {
    // Deklarasi binding untuk menghubungkan layout XML dengan kode
    private lateinit var binding: ActivityBookmarkBinding

    // Inisialisasi DAO untuk database bookmark menggunakan lazy initialization
    private val bookmarkDao by lazy {
        BookmarkRoomDatabase.getDatabase(this)?.bookmarkDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Mengaktifkan edge-to-edge UI
        enableEdgeToEdge()

        // Menghubungkan layout XML dengan kelas menggunakan view binding
        binding = ActivityBookmarkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mengatur RecyclerView untuk menampilkan daftar bookmark
        setupRecyclerView()

        // Mengamati perubahan data bookmark dari database
        bookmarkDao?.allBookmark?.observe(this) { bookmarks ->
            // Membuat adapter untuk RecyclerView dengan data dari bookmark
            val adapter = QuoteAdapter { _, _ -> }
            adapter.setQuotes(bookmarks.map {
                // Mengonversi data bookmark menjadi objek Quote
                Quote(it.id, it.english, it.indo, it.character, it.anime)
            }, bookmarks.map { it.id }.toSet())

            // Mengatur adapter untuk RecyclerView
            binding.rvBookmarks.adapter = adapter
        }
    }

    private fun setupRecyclerView() {
        // Mengatur layout manager untuk RecyclerView
        binding.rvBookmarks.layoutManager = LinearLayoutManager(this)
    }
}
