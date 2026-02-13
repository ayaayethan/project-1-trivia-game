package com.example.cst438_project_1.dbtests

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.cst438_project_1.data.db.AppDatabase

object TestDbFactory {
    fun inMemoryDb(): AppDatabase =
        Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
}