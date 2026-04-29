package com.adrscan.scanner

import android.graphics.RectF

data class QrResult(
    val content: String,
    val boundingBox: RectF,
    val isUrl: Boolean,
    val displayLabel: String
)
