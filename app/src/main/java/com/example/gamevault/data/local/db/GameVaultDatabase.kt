package com.example.gamevault.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.gamevault.data.local.entity.ReviewEntity

/**
 * Base de datos Room de GameVault.
 *
 * Usa el patrón Singleton para asegurar una única instancia en toda la app.
 * Esto es necesario porque crear múltiples instancias de la BD es costoso
 * y puede causar inconsistencias en los datos.
 */
@Database(
    entities = [ReviewEntity::class],
    version = 1,
    exportSchema = false
)
abstract class GameVaultDatabase : RoomDatabase() {

    abstract fun reviewDao(): ReviewDao

    companion object {
        @Volatile
        private var INSTANCE: GameVaultDatabase? = null

        /**
         * Obtiene la instancia única de la base de datos.
         * Si no existe, la crea de manera thread-safe con synchronized.
         */
        fun getDatabase(context: Context): GameVaultDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GameVaultDatabase::class.java,
                    "gamevault_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
