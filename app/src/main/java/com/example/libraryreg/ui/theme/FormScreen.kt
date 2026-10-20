
package com.example.libraryreg

import android.util.Patterns
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun FormScreen(
    db: MemberDatabaseHelper,
    anggota: Member? = null,
    onSimpan: () -> Unit,
    onKembali: () -> Unit
) {
    var nama by remember(anggota) {
        mutableStateOf(anggota?.nama ?: "")
    }
    var nomor by remember(anggota) {
        mutableStateOf(anggota?.nomorInduk ?: "")
    }
    var prodi by remember(anggota) {
        mutableStateOf(anggota?.prodi ?: "")
    }
    var alamat by remember(anggota) {
        mutableStateOf(anggota?.alamat ?: "")
    }
    var telepon by remember(anggota) {
        mutableStateOf(anggota?.telepon ?: "")
    }
    var email by remember(anggota) {
        mutableStateOf(anggota?.email ?: "")
    }
    var jenis by remember(anggota) {
        mutableStateOf(anggota?.jenis ?: "Mahasiswa")
    }
    var error by remember { mutableStateOf("") }
    var dropdown by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SoftBlue)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        IconButton(onClick = onKembali) {
            Icon(
                Icons.Default.ArrowBack,
                contentDescription = "Kembali"
            )
        }

        PageTitle(
            if (anggota == null) "Pendaftaran Anggota"
            else "Edit Anggota",
            "Lengkapi informasi anggota perpustakaan."
        )

        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = White
            )
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    "Informasi Pribadi",
                    style = MaterialTheme.typography.titleMedium,
                    color = Navy
                )

                LibraryField(nama, { nama = it }, "Nama Lengkap")
                LibraryField(nomor, { nomor = it }, "NIM / NIP")
                LibraryField(prodi, { prodi = it }, "Program Studi / Unit")

                LibraryField(
                    alamat, { alamat = it },
                    "Alamat", minLines = 2
                )

                LibraryField(
                    telepon, { telepon = it },
                    "Nomor Telepon",
                    keyboardType = KeyboardType.Phone
                )

                LibraryField(
                    email, { email = it },
                    "Email",
                    keyboardType = KeyboardType.Email
                )

                Text(
                    "Jenis Keanggotaan",
                    color = Navy,
                    style = MaterialTheme.typography.labelLarge
                )

                Box {
                    OutlinedButton(
                        onClick = { dropdown = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(jenis)
                        Spacer(Modifier.weight(1f))
                        Text("⌄")
                    }

                    DropdownMenu(
                        expanded = dropdown,
                        onDismissRequest = { dropdown = false }
                    ) {
                        listOf("Mahasiswa", "Dosen", "Staf")
                            .forEach { pilihan ->
                                DropdownMenuItem(
                                    text = { Text(pilihan) },
                                    onClick = {
                                        jenis = pilihan
                                        dropdown = false
                                    }
                                )
                            }
                    }
                }
            }
        }

        if (error.isNotEmpty()) {
            Text(
                error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        PrimaryButton(
            text = if (anggota == null)
                "Simpan Pendaftaran" else "Simpan Perubahan",
            onClick = {
                val n = nama.trim()
                val no = nomor.trim()
                val p = prodi.trim()
                val a = alamat.trim()
                val t = telepon.trim()
                val e = email.trim()

                error = when {
                    listOf(n, no, p, a, t, e)
                        .any { it.isBlank() } ->
                        "Semua kolom wajib diisi."

                    !no.matches(Regex("[A-Za-z0-9]{4,30}")) ->
                        "NIM/NIP harus 4-30 karakter huruf atau angka."

                    !Patterns.EMAIL_ADDRESS.matcher(e).matches() ->
                        "Format email tidak valid."

                    !t.matches(Regex("\\+?[0-9]{8,15}")) ->
                        "Nomor telepon harus 8-15 digit."

                    db.nomorSudahAda(no, anggota?.id ?: 0) ->
                        "NIM/NIP sudah terdaftar."

                    else -> ""
                }

                if (error.isEmpty()) {
                    val data = Member(
                        id = anggota?.id ?: 0,
                        nama = n,
                        nomorInduk = no,
                        prodi = p,
                        alamat = a,
                        telepon = t,
                        email = e,
                        jenis = jenis
                    )

                    val berhasil = if (anggota == null) {
                        db.tambah(data)
                    } else {
                        db.ubah(data.copy(id = anggota.id))
                    }

                    if (berhasil) {
                        onSimpan()
                    } else {
                        error = "Data gagal diperbarui."
                    }
                }
            }
        )

        OutlinedButton(
            onClick = onKembali,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Batal")
        }
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
fun LibraryField(
    value: String,
    onChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    minLines: Int = 1
) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        minLines = minLines,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White
        )
    )
}
