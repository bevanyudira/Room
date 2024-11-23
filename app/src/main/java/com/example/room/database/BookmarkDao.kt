package com.example.room.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

// Mendefinisikan Data Access Object (DAO) untuk entitas Bookmark
@Dao
interface BookmarkDao {
    // Menyisipkan data bookmark ke dalam database dengan strategi konflik IGNORE
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(bookmark: Bookmark)

    // Menghapus data bookmark dari database
    @Delete
    fun delete(bookmark: Bookmark)

    // Mendapatkan semua data bookmark dalam urutan ID secara menaik
    @get:Query("SELECT * from bookmark_table ORDER BY id ASC")
    val allBookmark: LiveData<List<Bookmark>>
}
