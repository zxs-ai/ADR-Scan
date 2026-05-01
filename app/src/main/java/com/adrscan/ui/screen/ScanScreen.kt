package com.adrscan.ui.screen

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adrscan.R
import com.adrscan.data.ScanDao
import com.adrscan.data.ScanRecord
import com.adrscan.scanner.QrResult
import com.adrscan.ui.component.*
import com.adrscan.ui.theme.SurfaceOverlay
import com.adrscan.ui.theme.TextPrimary
import com.adrscan.util.UrlHelper
import com.adrscan.util.VibrateHelper
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

// ── ViewModel ──────────────────────────────────────────────

@HiltViewModel
class ScanViewModel @Inject constructor(
    private val scanDao: ScanDao
) : ViewModel() {

    val records = scanDao.getAllRecords()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addRecord(content: String, isUrl: Boolean) {
        viewModelScope.launch {
            scanDao.insert(ScanRecord(content = content, isUrl = isUrl))
            // Enforce 500-record cap
            val count = scanDao.getCount()
            if (count > 500) {
                scanDao.deleteOldest(count - 500)
            }
        }
    }

    fun deleteRecord(record: ScanRecord) {
        viewModelScope.launch { scanDao.delete(record) }
    }

    fun clearAll() {
        viewModelScope.launch { scanDao.deleteAll() }
    }
}

// ── Main App Composable ────────────────────────────────────

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainApp(viewModel: ScanViewModel = hiltViewModel()) {
    val context = LocalContext.current

    // Build permission list based on API level
    val permissions = remember {
        buildList {
            add(Manifest.permission.CAMERA)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                add(Manifest.permission.POST_NOTIFICATIONS)
                add(Manifest.permission.READ_MEDIA_IMAGES)
            } else {
                add(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    val permissionsState = rememberMultiplePermissionsState(permissions)
    val cameraGranted = permissionsState.permissions
        .firstOrNull { it.permission == Manifest.permission.CAMERA }
        ?.status?.isGranted == true

    if (cameraGranted) {
        ScanScreen(viewModel = viewModel)
    } else {
        PermissionScreen(
            onRequestPermissions = { permissionsState.launchMultiplePermissionRequest() },
            onOpenSettings = {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", context.packageName, null)
                }
                context.startActivity(intent)
            },
            isPermanentlyDenied = !permissionsState.shouldShowRationale &&
                    permissionsState.permissions.any { !it.status.isGranted }
        )
    }
}

// ── Scan Screen ────────────────────────────────────────────

@Composable
fun ScanScreen(viewModel: ScanViewModel) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val records by viewModel.records.collectAsState()

    var isFlashOn by remember { mutableStateOf(false) }
    var historyExpanded by remember { mutableStateOf(false) }
    var qrResults by remember { mutableStateOf<List<QrResult>>(emptyList()) }
    var sheetContent by remember { mutableStateOf<Pair<String, Boolean>?>(null) }

    // Cooldown: prevent same QR from triggering within 5 seconds
    var lastProcessedContent by remember { mutableStateOf("") }
    var lastProcessedTime by remember { mutableStateOf(0L) }

    // Album picker
    val albumLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { processAlbumImage(context, it, viewModel) { content, isUrl ->
            if (isUrl) {
                openUrl(context, content)
            } else {
                sheetContent = content to false
            }
        }}
    }

    fun handleQrContent(content: String, isUrl: Boolean) {
        val now = System.currentTimeMillis()
        if (content == lastProcessedContent && now - lastProcessedTime < 5000) return
        lastProcessedContent = content
        lastProcessedTime = now

        VibrateHelper.vibrateShort(context)
        viewModel.addRecord(content, isUrl)

        if (isUrl) {
            openUrl(context, UrlHelper.normalizeUrl(content))
        } else {
            sheetContent = content to false
        }
    }

    // Auto-trigger for single QR code
    // Use frame counting for debounce
    var stableFrameCount by remember { mutableIntStateOf(0) }
    var stableContent by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(qrResults) {
        if (qrResults.size == 1) {
            val content = qrResults[0].content
            if (content == stableContent) {
                stableFrameCount++
                if (stableFrameCount >= 3) {
                    handleQrContent(content, qrResults[0].isUrl)
                    stableFrameCount = 0
                }
            } else {
                stableContent = content
                stableFrameCount = 1
            }
        } else {
            stableContent = null
            stableFrameCount = 0
        }
    }

    // Content sheet
    sheetContent?.let { (content, isUrl) ->
        ContentSheet(
            content = content,
            isUrl = isUrl,
            onDismiss = { sheetContent = null },
            onOpenUrl = if (isUrl) { url -> openUrl(context, UrlHelper.normalizeUrl(url)) } else null
        )
    }

    // Layout: portrait vs landscape
    if (isLandscape) {
        // Landscape: camera left, history right
        Row(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .weight(if (historyExpanded) 0.5f else 0.7f)
                    .fillMaxHeight()
            ) {
                CameraPreview(
                    modifier = Modifier.fillMaxSize(),
                    isFlashOn = isFlashOn,
                    onQrCodesDetected = { qrResults = it }
                )
                // QR overlay for multi-code
                if (qrResults.size >= 2) {
                    QrOverlay(
                        qrResults = qrResults,
                        onQrSelected = { handleQrContent(it.content, it.isUrl) }
                    )
                }
                // Top bar: flash + album
                Row(
                    modifier = Modifier
                        .statusBarsPadding()
                        .padding(16.dp)
                        .align(Alignment.TopEnd),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AlbumButton { albumLauncher.launch("image/*") }
                    FlashToggle(isOn = isFlashOn, onToggle = { isFlashOn = !isFlashOn })
                }
            }
            // History panel on right
            HistoryPanel(
                records = records,
                isExpanded = historyExpanded,
                onToggleExpand = { historyExpanded = !historyExpanded },
                onRecordClick = { record ->
                    if (record.isUrl) openUrl(context, UrlHelper.normalizeUrl(record.content))
                    else sheetContent = record.content to false
                },
                onDeleteRecord = { viewModel.deleteRecord(it) },
                onClearAll = { viewModel.clearAll() },
                modifier = Modifier
                    .weight(if (historyExpanded) 0.5f else 0.3f)
                    .fillMaxHeight()
            )
        }
    } else {
        // Portrait: camera top, history bottom
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .weight(if (historyExpanded) 0.3f else 0.7f)
                    .fillMaxWidth()
            ) {
                CameraPreview(
                    modifier = Modifier.fillMaxSize(),
                    isFlashOn = isFlashOn,
                    onQrCodesDetected = { qrResults = it }
                )
                if (qrResults.size >= 2) {
                    QrOverlay(
                        qrResults = qrResults,
                        onQrSelected = { handleQrContent(it.content, it.isUrl) }
                    )
                }
                Row(
                    modifier = Modifier
                        .statusBarsPadding()
                        .padding(16.dp)
                        .align(Alignment.TopEnd),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AlbumButton { albumLauncher.launch("image/*") }
                    FlashToggle(isOn = isFlashOn, onToggle = { isFlashOn = !isFlashOn })
                }
            }
            HistoryPanel(
                records = records,
                isExpanded = historyExpanded,
                onToggleExpand = { historyExpanded = !historyExpanded },
                onRecordClick = { record ->
                    if (record.isUrl) openUrl(context, UrlHelper.normalizeUrl(record.content))
                    else sheetContent = record.content to false
                },
                onDeleteRecord = { viewModel.deleteRecord(it) },
                onClearAll = { viewModel.clearAll() },
                modifier = Modifier
                    .weight(if (historyExpanded) 0.7f else 0.3f)
                    .fillMaxWidth()
            )
        }
    }
}

// ── Album Image Processing ────────────────────────────────

private fun processAlbumImage(
    context: Context,
    uri: Uri,
    viewModel: ScanViewModel,
    onResult: (String, Boolean) -> Unit
) {
    try {
        val image = InputImage.fromFilePath(context, uri)
        val scanner = BarcodeScanning.getClient()
        scanner.process(image)
            .addOnSuccessListener { barcodes ->
                if (barcodes.isNotEmpty()) {
                    val content = barcodes[0].rawValue ?: return@addOnSuccessListener
                    val isUrl = UrlHelper.isUrl(content)
                    viewModel.addRecord(content, isUrl)
                    onResult(content, isUrl)
                } else {
                    Toast.makeText(context, R.string.album_scan_failed, Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(context, R.string.album_scan_failed, Toast.LENGTH_SHORT).show()
            }
    } catch (e: Exception) {
        Toast.makeText(context, R.string.album_scan_failed, Toast.LENGTH_SHORT).show()
    }
}

// ── Helpers ────────────────────────────────────────────────

private fun openUrl(context: Context, url: String) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, R.string.no_browser, Toast.LENGTH_SHORT).show()
    }
}

@Composable
private fun AlbumButton(onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(SurfaceOverlay)
    ) {
        Icon(
            imageVector = Icons.Default.PhotoLibrary,
            contentDescription = null,
            tint = TextPrimary,
            modifier = Modifier.size(24.dp)
        )
    }
}

