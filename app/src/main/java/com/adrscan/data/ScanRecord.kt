package com.adrscan.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scan_records")
data class ScanRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val content: String,
    val isUrl: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)
