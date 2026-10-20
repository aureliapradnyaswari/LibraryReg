package com.example.libraryreg

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = MemberDatabaseHelper(this)

        setContent {
            MaterialTheme(colorScheme = LibraryColors) {

                var halaman by remember {
                    mutableStateOf("home")
                }

                var anggota by remember {
                    mutableStateOf<Member?>(null)
                }

                var refresh by remember {
                    mutableIntStateOf(0)
                }

                val daftarAnggota = remember(refresh) {
                    db.semua()
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = SoftBlue
                ) {
                    when (halaman) {

                        "home" -> HomeScreen(
                            totalAnggota = daftarAnggota.size,
                            onDaftar = {
                                anggota = null
                                halaman = "form"
                            },
                            onLihat = {
                                halaman = "list"
                            }
                        )

                        "form" -> key(anggota?.id ?: -1) {
                            FormScreen(
                                db = db,
                                anggota = anggota,
                                onSimpan = {
                                    refresh++
                                    anggota = null
                                    halaman = "list"
                                },
                                onKembali = {
                                    halaman = if (anggota == null)
                                        "home"
                                    else "detail"
                                }
                            )
                        }


                        "list" -> key(refresh) {
                            ListScreen(
                                db = db,

                                onDetail = { data ->
                                    anggota = data
                                    halaman = "detail"
                                },

                                onEdit = { data ->
                                    anggota = data
                                    halaman = "form"
                                },

                                onTambah = {
                                    anggota = null
                                    halaman = "form"
                                },

                                onKembali = {
                                    halaman = "home"
                                }
                            )
                        }

                        "detail" -> {
                            anggota?.let { data ->
                                DetailScreen(
                                    anggota = data,
                                    onEdit = {
                                        halaman = "form"
                                    },
                                    onHapus = {
                                        if (db.hapus(data.id)) {
                                            refresh++
                                            anggota = null
                                            halaman = "list"
                                        }
                                    },
                                    onKembali = {
                                        halaman = "list"
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}