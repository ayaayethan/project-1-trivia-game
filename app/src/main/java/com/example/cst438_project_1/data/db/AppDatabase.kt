package com.example.cst438_project_1.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [UserEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        private val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE users ADD COLUMN bestScore INTEGER NOT NULL DEFAULT 0")
            }
        }

        fun get(context: Context): AppDatabase {
            val existing = INSTANCE
            if (existing != null) return existing

            return synchronized(this) {
                val again = INSTANCE
                if (again != null) return@synchronized again

                val created = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "game_diff.db"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()

                INSTANCE = created
                created
            }
        }
    }
}