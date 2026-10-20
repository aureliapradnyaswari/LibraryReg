package com.example.libraryreg

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MemberDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "libraryreg.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE anggota (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nama TEXT NOT NULL,
                nomorInduk TEXT NOT NULL UNIQUE,
                prodi TEXT NOT NULL,
                alamat TEXT NOT NULL,
                telepon TEXT NOT NULL,
                email TEXT NOT NULL,
                jenis TEXT NOT NULL
            )
        """.trimIndent())
    }

    override fun onUpgrade(
        db: SQLiteDatabase, oldVersion: Int, newVersion: Int
    ) {
        db.execSQL("DROP TABLE IF EXISTS anggota")
        onCreate(db)
    }

    private fun values(m: Member) = ContentValues().apply {
        put("nama", m.nama)
        put("nomorInduk", m.nomorInduk)
        put("prodi", m.prodi)
        put("alamat", m.alamat)
        put("telepon", m.telepon)
        put("email", m.email)
        put("jenis", m.jenis)
    }

    fun tambah(m: Member): Boolean =
        writableDatabase.insert(
            "anggota", null, values(m)
        ) != -1L


    fun ubah(m: Member): Boolean {
        val data = ContentValues().apply {
            put("nama", m.nama)
            put("nomorInduk", m.nomorInduk)
            put("prodi", m.prodi)
            put("alamat", m.alamat)
            put("telepon", m.telepon)
            put("email", m.email)
            put("jenis", m.jenis)
        }

        val hasil = writableDatabase.update(
            "anggota",
            data,
            "id = ?",
            arrayOf(m.id.toString())
        )
        return hasil > 0
    }


    fun hapus(id: Int): Boolean =
        writableDatabase.delete(
            "anggota", "id = ?",
            arrayOf(id.toString())
        ) > 0

    fun semua(): List<Member> {
        val hasil = mutableListOf<Member>()
        readableDatabase.rawQuery(
            "SELECT * FROM anggota ORDER BY nama", null
        ).use { cursor ->
            while (cursor.moveToNext()) {
                hasil.add(Member(
                    id = cursor.getInt(0),
                    nama = cursor.getString(1),
                    nomorInduk = cursor.getString(2),
                    prodi = cursor.getString(3),
                    alamat = cursor.getString(4),
                    telepon = cursor.getString(5),
                    email = cursor.getString(6),
                    jenis = cursor.getString(7)
                ))
            }
        }
        return hasil
    }

    fun nomorSudahAda(nomor: String, id: Int = 0): Boolean =
        semua().any {
            it.nomorInduk == nomor && it.id != id
        }
}