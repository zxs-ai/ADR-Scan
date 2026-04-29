package com.adrscan.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Database(entities = [ScanRecord::class], version = 1, exportSchema = false)
abstract class ScanDatabase : RoomDatabase() {
    abstract fun scanDao(): ScanDao
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ScanDatabase {
        return Room.databaseBuilder(
            context,
            ScanDatabase::class.java,
            "adrscan.db"
        ).build()
    }

    @Provides
    fun provideScanDao(database: ScanDatabase): ScanDao {
        return database.scanDao()
    }
}
