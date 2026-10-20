package com.example.libraryreg

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun DetailScreen(
    anggota: Member,
    onEdit: () -> Unit,
    onHapus: () -> Unit,
    onKembali: () -> Unit
) {
    var konfirmasi by remember { mutableStateOf(false) }

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
            "Detail Anggota",
            "Informasi keanggotaan perpustakaan."
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(
                containerColor = White
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                InitialAvatar(
                    anggota.nama,
                    modifier = Modifier.size(84.dp)
                )

                Spacer(Modifier.height(14.dp))

                Text(
                    anggota.nama,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Navy
                )

                Text(anggota.nomorInduk, color = TextGray)

                Spacer(Modifier.height(12.dp))

                Surface(
                    color = SoftBlue,
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        anggota.jenis,
                        modifier = Modifier.padding(
                            horizontal = 16.dp,
                            vertical = 8.dp
                        ),
                        color = PrimaryBlue,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(
                containerColor = White
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    "Informasi Lengkap",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Navy
                )

                InfoRow("Nama Lengkap", anggota.nama)
                InfoRow("NIM/NIP", anggota.nomorInduk)
                InfoRow("Program Studi / Unit", anggota.prodi)
                InfoRow("Alamat", anggota.alamat)
                InfoRow("Nomor Telepon", anggota.telepon)
                InfoRow("Email", anggota.email)
                InfoRow("Jenis Keanggotaan", anggota.jenis)
            }
        }

        PrimaryButton(
            text = "Edit Anggota",
            onClick = onEdit
        )

        OutlinedButton(
            onClick = { konfirmasi = true },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color(0xFFD32F2F)
            )
        ) {
            Icon(
                Icons.Default.Delete,
                contentDescription = null
            )
            Spacer(Modifier.width(8.dp))
            Text("Hapus Anggota")
        }

        Spacer(Modifier.height(12.dp))
    }

    if (konfirmasi) {
        AlertDialog(
            onDismissRequest = { konfirmasi = false },
            title = { Text("Hapus Anggota?") },
            text = {
                Text(
                    "Yakin ingin menghapus ${anggota.nama}? " +
                            "Data tidak dapat dikembalikan."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        konfirmasi = false
                        onHapus()
                    }
                ) {
                    Text(
                        "Hapus",
                        color = Color(0xFFD32F2F)
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { konfirmasi = false }
                ) {
                    Text("Batal")
                }
            }
        )
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Column {
        Text(
            label,
            color = TextGray,
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(Modifier.height(3.dp))
        Text(
            value,
            color = Navy,
            fontWeight = FontWeight.Medium
        )
        HorizontalDivider(
            modifier = Modifier.padding(top = 12.dp),
            color = SoftBlue
        )
    }
}