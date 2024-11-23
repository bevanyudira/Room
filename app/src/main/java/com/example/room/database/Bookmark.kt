package com.example.room.database

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// Mendefinisikan entitas Bookmark yang akan merepresentasikan tabel dalam database
@Entity(tableName = "bookmark_table")
data class Bookmark(
    // Kolom ID sebagai primary key dengan auto-generate
    @PrimaryKey(autoGenerate = true)
    @NonNull
    val id: Int = 0,

    // Kolom untuk menyimpan karakter kutipan
    @ColumnInfo(name = "character")
    val character: String,

    // Kolom untuk menyimpan nama anime dari kutipan
    @ColumnInfo(name = "anime")
    val anime: String,

    // Kolom untuk menyimpan kutipan dalam bahasa Inggris
    @ColumnInfo(name = "english")
    val english: String,

    // Kolom untuk menyimpan kutipan dalam bahasa Indonesia
    @ColumnInfo(name = "indo")
    val indo: String,
)
