package com.example.libraryreg

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp


@Composable
fun ListScreen(
    db: MemberDatabaseHelper,
    onDetail: (Member) -> Unit,
    onEdit: (Member) -> Unit,
    onTambah: () -> Unit,
    onKembali: () -> Unit
) {
    var cari by remember { mutableStateOf("") }
    val anggota = remember(db) { db.semua() }

    val hasil = anggota.filter {
        it.nama.contains(cari, ignoreCase = true) ||
                it.nomorInduk.contains(cari, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SoftBlue)
            .padding(20.dp)
    ) {
        IconButton(onClick = onKembali) {
            Icon(
                Icons.Default.ArrowBack,
                contentDescription = "Kembali"
            )
        }

        PageTitle(
            "Daftar Anggota",
            "Kelola seluruh anggota perpustakaan."
        )

        Spacer(Modifier.height(20.dp))

        OutlinedTextField(
            value = cari,
            onValueChange = { cari = it },
            label = { Text("Cari nama atau NIM/NIP") },
            leadingIcon = {
                Icon(
                    Icons.Default.Search,
                    contentDescription = null
                )
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            singleLine = true
        )

        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "${hasil.size} Anggota",
                fontWeight = FontWeight.Bold,
                color = Navy,
                modifier = Modifier.weight(1f)
            )

            FilledTonalIconButton(onClick = onTambah) {
                Icon(
                    Icons.Default.PersonAdd,
                    contentDescription = "Tambah anggota"
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        if (hasil.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    if (cari.isEmpty())
                        "Belum ada anggota terdaftar."
                    else
                        "Anggota tidak ditemukan.",
                    color = TextGray
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(hasil, key = { it.id }) { member ->
                    MemberCard(
                        member = member,
                        onClick = { onDetail(member) },
                        onEdit = { onEdit(member) }
                    )
                }
            }
        }

        Spacer(Modifier.height(12.dp))
        PrimaryButton(
            text = "+ Tambah Anggota",
            onClick = onTambah
        )
    }
}

@Composable
fun MemberCard(
    member: Member,
    onClick: () -> Unit,
    onEdit: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = White
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(Modifier.padding(16.dp)) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                InitialAvatar(member.nama)

                Spacer(Modifier.width(14.dp))

                Column(Modifier.weight(1f)) {
                    Text(
                        member.nama,
                        fontWeight = FontWeight.Bold,
                        color = Navy
                    )

                    Text(
                        member.nomorInduk,
                        color = TextGray
                    )

                    Text(
                        member.jenis,
                        color = PrimaryBlue
                    )
                }
            }

            Spacer(Modifier.height(12.dp))
            HorizontalDivider()
            Spacer(Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onClick,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Detail")
                }

                Button(
                    onClick = onEdit,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text("Edit")
                }
            }
        }
    }
}
