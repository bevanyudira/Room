package com.example.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase

// Mendefinisikan database Room dengan entitas Bookmark
@Database(entities = [Bookmark::class], version = 1, exportSchema = false)
abstract class BookmarkRoomDatabase : RoomDatabase() {
    // Deklarasi fungsi abstrak untuk mengakses DAO Bookmark
    abstract fun bookmarkDao(): BookmarkDao?

    companion object {
        // Variabel singleton untuk mencegah pembuatan instance database yang berulang
        @Volatile
        private var INSTANCE: BookmarkRoomDatabase? = null

        // Fungsi untuk mendapatkan instance database
        fun getDatabase(context: Context): BookmarkRoomDatabase? {
            if (INSTANCE == null) {
                // Sinkronisasi untuk memastikan hanya satu instance database yang dibuat
                synchronized(BookmarkRoomDatabase::class.java) {
                    INSTANCE = databaseBuilder(
                        context.applicationContext,
                        BookmarkRoomDatabase::class.java,
                        "bookmark_database"
                    ).build()
                }
            }
            return INSTANCE
        }
    }
}
