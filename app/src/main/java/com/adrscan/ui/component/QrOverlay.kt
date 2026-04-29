package com.adrscan.ui.component

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adrscan.scanner.QrResult
import com.adrscan.ui.theme.AccentGlow
import com.adrscan.ui.theme.AccentMain
import com.adrscan.ui.theme.SurfaceOverlay
import com.adrscan.ui.theme.TextPrimary
import com.adrscan.ui.theme.UrlColor

@Composable
fun QrOverlay(
    modifier: Modifier = Modifier,
    qrResults: List<QrResult>,
    onQrSelected: (QrResult) -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        qrResults.forEach { result ->
            QrSelectionButton(
                result = result,
                onClick = { onQrSelected(result) },
                modifier = Modifier.align(Alignment.TopStart)
            )
        }
    }
}

@Composable
private fun QrSelectionButton(
    result: QrResult,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Pulsing glow animation
    val infiniteTransition = rememberInfiniteTransition(label = "glow")
    val glowScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowScale"
    )
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowAlpha"
    )

    // Appear animation
    var appeared by remember { mutableStateOf(false) }
    val appearScale by animateFloatAsState(
        targetValue = if (appeared) 1f else 0f,
        animationSpec = tween(200, easing = EaseOut),
        label = "appear"
    )
    LaunchedEffect(Unit) { appeared = true }

    // Position the button at the center of the QR bounding box
    val box = result.boundingBox
    val centerX = (box.left + box.right) / 2f
    val centerY = (box.top + box.bottom) / 2f

    Box(
        modifier = modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.TopStart)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(centerX)
                .fillMaxHeight(centerY)
        )

        // Glow layer
        Box(
            modifier = Modifier
                .offset(
                    x = (centerX * 1000).dp / 1000 - 60.dp,
                    y = (centerY * 1000).dp / 1000 - 20.dp
                )
                .scale(appearScale)
        ) {
            // Outer glow
            Box(
                modifier = Modifier
                    .size(120.dp, 40.dp)
                    .scale(glowScale)
                    .background(
                        color = AccentGlow.copy(alpha = glowAlpha),
                        shape = RoundedCornerShape(20.dp)
                    )
            )
            // Button
            Box(
                modifier = Modifier
                    .size(120.dp, 40.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                SurfaceOverlay,
                                Color(0xCC1C1F26)
                            )
                        )
                    )
                    .border(1.dp, AccentMain.copy(alpha = 0.6f), RoundedCornerShape(20.dp))
                    .clickable(onClick = onClick),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = result.displayLabel,
                    color = if (result.isUrl) UrlColor else TextPrimary,
                    fontSize = 11.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
    }
}
