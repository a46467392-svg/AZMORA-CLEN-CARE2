package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyberBlack

@Composable
fun BrandAppIconBadge(
    packageName: String,
    modifier: Modifier = Modifier,
    size: Dp = 44.dp
) {
    when {
        // Google Files / System Files: Signature Google Blue folder with Cyan/Yellow folder tab
        packageName.contains("nbu.files") || packageName.contains("myfiles") -> {
            Box(
                modifier = modifier
                    .size(size)
                    .clip(RoundedCornerShape(size * 0.28f))
                    .background(Color(0xFF1A73E8))
                    .border(1.2.dp, Color(0xFF34A853), RoundedCornerShape(size * 0.28f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.FolderSpecial,
                    contentDescription = "Files",
                    tint = Color.White,
                    modifier = Modifier.size(size * 0.62f)
                )
            }
        }
        // YouTube: Red brand background with White Play triangle
        packageName.contains("youtube") -> {
            Box(
                modifier = modifier
                    .size(size)
                    .clip(RoundedCornerShape(size * 0.28f))
                    .background(Color(0xFFFF0000))
                    .border(1.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(size * 0.28f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "YouTube",
                    tint = Color.White,
                    modifier = Modifier.size(size * 0.6f)
                )
            }
        }

        // Facebook: Bold royal blue brand with White 'f'
        packageName.contains("facebook") -> {
            Box(
                modifier = modifier
                    .size(size)
                    .clip(CircleShape)
                    .background(Color(0xFF1877F2))
                    .border(1.dp, Color.White.copy(alpha = 0.3f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "f",
                    color = Color.White,
                    fontSize = (size.value * 0.62f).sp,
                    fontWeight = FontWeight.Black
                )
            }
        }

        // TikTok: Iconic Dark Obsidian with Neon Cyan & Pink neon vibes
        packageName.contains("tiktok") -> {
            Box(
                modifier = modifier
                    .size(size)
                    .clip(RoundedCornerShape(size * 0.28f))
                    .background(Color(0xFF010101))
                    .border(1.5.dp, Color(0xFF00F2FE), RoundedCornerShape(size * 0.28f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.MusicNote,
                    contentDescription = "TikTok",
                    tint = Color(0xFFFE2C55),
                    modifier = Modifier.size(size * 0.58f)
                )
            }
        }

        // Google Chrome: Vibrant multi-tone yellow/green/red with blue core
        packageName.contains("chrome") -> {
            Box(
                modifier = modifier
                    .size(size)
                    .clip(CircleShape)
                    .background(Color(0xFF4285F4))
                    .border(1.5.dp, Color(0xFF34A853), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(size * 0.55f)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(size * 0.36f)
                            .clip(CircleShape)
                            .background(Color(0xFF1A73E8))
                    )
                }
            }
        }

        // Google Gmail: White background with bold Red M
        packageName.contains("gm") || packageName.contains("mail") -> {
            Box(
                modifier = modifier
                    .size(size)
                    .clip(RoundedCornerShape(size * 0.28f))
                    .background(Color(0xFFFFFFFF))
                    .border(1.dp, Color(0xFFEA4335), RoundedCornerShape(size * 0.28f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Mail,
                    contentDescription = "Gmail",
                    tint = Color(0xFFEA4335),
                    modifier = Modifier.size(size * 0.62f)
                )
            }
        }

        // WhatsApp: Vibrant Emerald Green with phone chat icon
        packageName.contains("whatsapp") -> {
            Box(
                modifier = modifier
                    .size(size)
                    .clip(CircleShape)
                    .background(Color(0xFF25D366))
                    .border(1.dp, Color.White.copy(alpha = 0.3f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Chat,
                    contentDescription = "WhatsApp",
                    tint = Color.White,
                    modifier = Modifier.size(size * 0.58f)
                )
            }
        }

        // Google Photos: Multi-color pinwheel style
        packageName.contains("photos") -> {
            Box(
                modifier = modifier
                    .size(size)
                    .clip(RoundedCornerShape(size * 0.28f))
                    .background(Color(0xFFFFFFFF))
                    .border(1.5.dp, Color(0xFFFBBC05), RoundedCornerShape(size * 0.28f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PhotoLibrary,
                    contentDescription = "Google Photos",
                    tint = Color(0xFF4285F4),
                    modifier = Modifier.size(size * 0.58f)
                )
            }
        }

        // Spotify: Spotify Green with audio wave
        packageName.contains("spotify") -> {
            Box(
                modifier = modifier
                    .size(size)
                    .clip(CircleShape)
                    .background(Color(0xFF1DB954)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.GraphicEq,
                    contentDescription = "Spotify",
                    tint = Color.Black,
                    modifier = Modifier.size(size * 0.58f)
                )
            }
        }

        // Clean Care App itself: Neon Cyan Cyber Emblem
        else -> {
            Box(
                modifier = modifier
                    .size(size)
                    .clip(RoundedCornerShape(size * 0.28f))
                    .background(Color(0xFF0F172A))
                    .border(1.dp, Color(0xFF00F0FF), RoundedCornerShape(size * 0.28f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Android,
                    contentDescription = "App",
                    tint = Color(0xFF00F0FF),
                    modifier = Modifier.size(size * 0.58f)
                )
            }
        }
    }
}
