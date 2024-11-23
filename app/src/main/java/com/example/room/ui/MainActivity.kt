package com.example.room.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.room.database.Bookmark
import com.example.room.database.BookmarkRoomDatabase
import com.example.room.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import model.Quote
import model.QuoteResponse
import network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    // Deklarasi binding untuk menghubungkan layout XML dengan kode
    private lateinit var binding: ActivityMainBinding

    // Deklarasi adapter untuk RecyclerView
    private lateinit var quoteAdapter: QuoteAdapter

    // Inisialisasi DAO untuk database bookmark menggunakan lazy initialization
    private val bookmarkDao by lazy {
        BookmarkRoomDatabase.getDatabase(this)?.bookmarkDao()
    }

    // Menyimpan daftar kutipan yang didapat dari API
    private var quotesList: List<Quote> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Mengaktifkan edge-to-edge UI
        enableEdgeToEdge()

        // Menghubungkan layout XML dengan kelas menggunakan view binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mengatur RecyclerView untuk menampilkan daftar kutipan
        setupRecyclerView()

        // Memanggil fungsi untuk mengambil kutipan dan bookmark dari API dan database
        fetchQuotes()

        // Menambahkan listener untuk tombol bookmark yang membuka BookmarkActivity
        binding.btnBookmark.setOnClickListener {
            val intent = Intent(this, BookmarkActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupRecyclerView() {
        // Menginisialisasi adapter dengan callback untuk menangani bookmark
        quoteAdapter = QuoteAdapter { quote, isBookmarked ->
            handleBookmarkAction(quote, isBookmarked)
        }

        // Mengatur layout manager dan adapter untuk RecyclerView
        binding.rvQuote.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = quoteAdapter
        }
    }

    private fun fetchQuotes() {
        // Memanggil API untuk mendapatkan data kutipan
        val apiService = ApiClient.getInstance()
        val call = apiService.getKata("tidak akan", 1)

        // Menggunakan enqueue untuk menangani respons API secara asinkron
        call.enqueue(object : Callback<QuoteResponse> {
            override fun onResponse(call: Call<QuoteResponse>, response: Response<QuoteResponse>) {
                // Mengecek apakah respons API berhasil
                if (response.isSuccessful) {
                    // Jika berhasil, simpan data kutipan dan update RecyclerView
                    response.body()?.result?.let { quotes ->
                        quotesList = quotes

                        quoteAdapter.setQuotes(quotes, emptySet())

                        // Memanggil fungsi untuk mengambil data bookmark dari database
                        fetchBookmarks()
                    }
                } else {
                    // Menampilkan pesan kesalahan jika respons gagal
                    Toast.makeText(this@MainActivity, "Failed to load quotes", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<QuoteResponse>, t: Throwable) {
                // Menampilkan pesan kesalahan jika ada masalah dengan API
                Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun fetchBookmarks() {
        // Mengamati data bookmark dari database menggunakan LiveData
        bookmarkDao?.allBookmark?.observe(this) { bookmarks ->
            // Mengonversi daftar bookmark menjadi ID yang dibookmark
            val bookmarkedIds = bookmarks.map { it.id }.toSet()

            // Memperbarui RecyclerView dengan data kutipan dan status bookmark
            quoteAdapter.setQuotes(quotesList, bookmarkedIds)
        }
    }

    private fun handleBookmarkAction(quote: Quote, isBookmarked: Boolean) {
        // Menangani aksi bookmark secara asinkron menggunakan Coroutine
        CoroutineScope(Dispatchers.IO).launch {
            if (isBookmarked) {
                // Menambahkan kutipan ke database jika dibookmark
                bookmarkDao?.insert(
                    Bookmark(
                        character = quote.character,
                        anime = quote.anime,
                        english = quote.english,
                        indo = quote.indo
                    )
                )
            } else {
                // Menghapus kutipan dari database jika dihapus dari bookmark
                bookmarkDao?.delete(
                    Bookmark(
                        character = quote.character,
                        anime = quote.anime,
                        english = quote.english,
                        indo = quote.indo
                    )
                )
            }
        }
    }
}
