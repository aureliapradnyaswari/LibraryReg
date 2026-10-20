package com.example.libraryreg

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

val Navy = Color(0xFF102C54)
val PrimaryBlue = Color(0xFF2478E8)
val SoftBlue = Color(0xFFEEF5FF)
val TextGray = Color(0xFF64748B)
val White = Color.White

val LibraryColors = lightColorScheme(
    primary = PrimaryBlue,
    onPrimary = White,
    background = SoftBlue,
    onBackground = Navy,
    surface = White,
    onSurface = Navy
)

@Composable
fun PageTitle(title: String, subtitle: String) {
    Column {
        Text(
            title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Navy
        )
        Spacer(Modifier.height(4.dp))
        Text(subtitle, color = TextGray)
    }
}

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(14.dp)
    ) {
        Text(text, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun InitialAvatar(
    name: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(52.dp)
            .background(
                Color(0xFFDCEAFF),
                RoundedCornerShape(16.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = name.trim()
                .take(1)
                .uppercase()
                .ifEmpty { "?" },
            color = PrimaryBlue,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
    }
}