package com.adrscan.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ScanDao {
    @Query("SELECT * FROM scan_records ORDER BY timestamp DESC")
    fun getAllRecords(): Flow<List<ScanRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: ScanRecord)

    @Delete
    suspend fun delete(record: ScanRecord)

    @Query("DELETE FROM scan_records")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM scan_records")
    suspend fun getCount(): Int

    @Query("DELETE FROM scan_records WHERE id IN (SELECT id FROM scan_records ORDER BY timestamp ASC LIMIT :count)")
    suspend fun deleteOldest(count: Int)
}
