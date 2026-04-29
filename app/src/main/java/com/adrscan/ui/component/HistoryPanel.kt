package com.adrscan.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.adrscan.R
import com.adrscan.data.ScanRecord
import com.adrscan.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HistoryPanel(
    records: List<ScanRecord>,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    onRecordClick: (ScanRecord) -> Unit,
    onDeleteRecord: (ScanRecord) -> Unit,
    onClearAll: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showClearDialog by remember { mutableStateOf(false) }

    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            title = { Text(stringResource(R.string.clear_all)) },
            text = { Text(stringResource(R.string.clear_all_confirm)) },
            confirmButton = {
                TextButton(onClick = {
                    onClearAll()
                    showClearDialog = false
                }) { Text(stringResource(R.string.confirm), color = ErrorRed) }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            },
            containerColor = SurfaceCard,
            titleContentColor = TextPrimary,
            textContentColor = TextSecondary
        )
    }

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        SurfaceCard.copy(alpha = 0.95f),
                        SurfaceDark.copy(alpha = 0.98f)
                    )
                )
            )
    ) {
        // Handle bar + title
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onToggleExpand)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowDown
                        else Icons.Default.KeyboardArrowUp,
                    contentDescription = null,
                    tint = AccentMain,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.scan_history),
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary
                )
                if (records.isNotEmpty()) {
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "(${records.size})",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMuted
                    )
                }
            }
            if (records.isNotEmpty()) {
                IconButton(
                    onClick = { showClearDialog = true },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        Icons.Default.DeleteSweep,
                        contentDescription = stringResource(R.string.clear_all),
                        tint = TextMuted,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        HorizontalDivider(color = TextMuted.copy(alpha = 0.2f), thickness = 0.5.dp)

        if (records.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.no_history),
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextMuted
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
            ) {
                items(records, key = { it.id }) { record ->
                    HistoryItem(
                        record = record,
                        onClick = { onRecordClick(record) },
                        onDelete = { onDeleteRecord(record) }
                    )
                }
            }
        }
    }
}

@Composable
private fun HistoryItem(
    record: ScanRecord,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("MM-dd HH:mm", Locale.getDefault()) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (record.isUrl) Icons.Default.Link else Icons.Default.Notes,
            contentDescription = null,
            tint = if (record.isUrl) UrlColor else TextContentColor,
            modifier = Modifier.size(18.dp)
        )
        Spacer(Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = record.content,
                style = MaterialTheme.typography.bodyMedium,
                color = if (record.isUrl) UrlColor else TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = dateFormat.format(Date(record.timestamp)),
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted
            )
        }
        IconButton(
            onClick = onDelete,
            modifier = Modifier.size(28.dp)
        ) {
            Icon(
                Icons.Default.Delete,
                contentDescription = null,
                tint = TextMuted,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}
