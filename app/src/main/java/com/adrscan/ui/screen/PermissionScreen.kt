package com.adrscan.ui.screen

import android.Manifest
import android.os.Build
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.adrscan.R
import com.adrscan.ui.theme.*

@Composable
fun PermissionScreen(
    onRequestPermissions: () -> Unit,
    onOpenSettings: () -> Unit,
    isPermanentlyDenied: Boolean = false
) {
    // Breathing animation for the camera icon
    val infiniteTransition = rememberInfiniteTransition(label = "breathe")
    val iconScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "iconScale"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(PrimaryDark, SurfaceDark)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 400.dp)
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // App icon with glow
            Box(contentAlignment = Alignment.Center) {
                // Glow background
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .scale(iconScale * 1.2f)
                        .clip(CircleShape)
                        .background(AccentGlow)
                )
                // Icon circle
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .scale(iconScale)
                        .clip(CircleShape)
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(AccentMain, PrimaryLight)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.CameraAlt,
                        contentDescription = null,
                        tint = TextPrimary,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }

            Spacer(Modifier.height(32.dp))

            Text(
                text = "ADR-Scan",
                style = MaterialTheme.typography.headlineLarge,
                color = TextPrimary
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "一扫即达，快速识别",
                style = MaterialTheme.typography.bodyLarge,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(40.dp))

            // Permission items
            PermissionItem(
                icon = Icons.Default.CameraAlt,
                text = stringResource(R.string.permission_camera_desc)
            )
            Spacer(Modifier.height(12.dp))
            PermissionItem(
                icon = Icons.Default.PhotoLibrary,
                text = stringResource(R.string.permission_storage_desc)
            )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Spacer(Modifier.height(12.dp))
                PermissionItem(
                    icon = Icons.Default.Notifications,
                    text = stringResource(R.string.permission_notification_desc)
                )
            }

            Spacer(Modifier.height(48.dp))

            // Action button
            Button(
                onClick = if (isPermanentlyDenied) onOpenSettings else onRequestPermissions,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(26.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AccentMain,
                    contentColor = SurfaceDark
                )
            ) {
                Text(
                    text = stringResource(
                        if (isPermanentlyDenied) R.string.permission_btn_settings
                        else R.string.permission_btn_start
                    ),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
private fun PermissionItem(icon: ImageVector, text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceCard.copy(alpha = 0.6f))
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = AccentMain,
            modifier = Modifier.size(24.dp)
        )
        Spacer(Modifier.width(14.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = TextPrimary
        )
    }
}
