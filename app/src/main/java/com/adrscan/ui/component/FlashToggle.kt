package com.adrscan.ui.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.adrscan.ui.theme.SurfaceOverlay
import com.adrscan.ui.theme.TextContentColor
import com.adrscan.ui.theme.TextMuted

@Composable
fun FlashToggle(
    isOn: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val iconTint by animateColorAsState(
        targetValue = if (isOn) TextContentColor else TextMuted,
        label = "flashColor"
    )

    IconButton(
        onClick = onToggle,
        modifier = modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(SurfaceOverlay)
    ) {
        Icon(
            imageVector = if (isOn) Icons.Default.FlashOn else Icons.Default.FlashOff,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(24.dp)
        )
    }
}
