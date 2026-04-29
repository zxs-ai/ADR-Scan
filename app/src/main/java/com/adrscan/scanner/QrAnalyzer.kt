package com.adrscan.scanner

import android.graphics.RectF
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.adrscan.util.UrlHelper
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage

class QrAnalyzer(
    private val onQrCodesDetected: (List<QrResult>) -> Unit
) : ImageAnalysis.Analyzer {

    private val scanner = BarcodeScanning.getClient()
    private var isProcessing = false

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        if (isProcessing) {
            imageProxy.close()
            return
        }

        val mediaImage = imageProxy.image
        if (mediaImage == null) {
            imageProxy.close()
            return
        }

        isProcessing = true

        val image = InputImage.fromMediaImage(
            mediaImage,
            imageProxy.imageInfo.rotationDegrees
        )

        scanner.process(image)
            .addOnSuccessListener { barcodes ->
                val results = barcodes.mapNotNull { barcode ->
                    val content = barcode.rawValue ?: return@mapNotNull null
                    val rect = barcode.boundingBox ?: return@mapNotNull null

                    val isUrl = UrlHelper.isUrl(content)
                    val displayLabel = UrlHelper.getDisplayLabel(content, isUrl)

                    // Normalize bounding box to 0..1 range
                    val imageWidth = imageProxy.width.toFloat()
                    val imageHeight = imageProxy.height.toFloat()

                    QrResult(
                        content = content,
                        boundingBox = RectF(
                            rect.left / imageWidth,
                            rect.top / imageHeight,
                            rect.right / imageWidth,
                            rect.bottom / imageHeight
                        ),
                        isUrl = isUrl,
                        displayLabel = displayLabel
                    )
                }
                onQrCodesDetected(results)
            }
            .addOnFailureListener { /* retry on next frame */ }
            .addOnCompleteListener {
                isProcessing = false
                imageProxy.close()
            }
    }
}
