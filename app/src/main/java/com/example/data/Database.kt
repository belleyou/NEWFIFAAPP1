package com.example.data

import android.content.Context
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.Room
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "favorite_teams")
data class FavoriteTeam(
    @PrimaryKey val abbreviation: String,
    val name: String,
    val flag: String,
    val isNotificationEnabled: Boolean = true
)

@Entity(tableName = "match_notifications")
data class MatchNotification(
    @PrimaryKey val matchId: String, // e.g. "ARG-FRA" or unique identifier
    val teamAbbreviation: String,
    val opponent: String,
    val dateStr: String,
    val timeStr: String,
    val kickoffEpoch: Long,
    val isNotificationEnabled: Boolean = true
)

@Dao
interface FavoritesDao {
    @Query("SELECT * FROM favorite_teams")
    fun getFavoriteTeamsFlow(): Flow<List<FavoriteTeam>>

    @Query("SELECT * FROM favorite_teams")
    suspend fun getFavoriteTeams(): List<FavoriteTeam>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(team: FavoriteTeam)

    @Query("DELETE FROM favorite_teams WHERE abbreviation = :abbreviation")
    suspend fun deleteFavorite(abbreviation: String)

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_teams WHERE abbreviation = :abbreviation LIMIT 1)")
    fun isFavoriteFlow(abbreviation: String): Flow<Boolean>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_teams WHERE abbreviation = :abbreviation LIMIT 1)")
    suspend fun isFavorite(abbreviation: String): Boolean
}

@Dao
interface MatchNotificationDao {
    @Query("SELECT * FROM match_notifications")
    fun getMatchNotificationsFlow(): Flow<List<MatchNotification>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(matchNotification: MatchNotification)

    @Query("DELETE FROM match_notifications WHERE matchId = :matchId")
    suspend fun deleteNotification(matchId: String)

    @Query("SELECT EXISTS(SELECT 1 FROM match_notifications WHERE matchId = :matchId LIMIT 1)")
    fun isNotificationEnabledFlow(matchId: String): Flow<Boolean>

    @Query("SELECT * FROM match_notifications WHERE matchId = :matchId LIMIT 1")
    suspend fun getNotification(matchId: String): MatchNotification?
}

@Database(entities = [FavoriteTeam::class, MatchNotification::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoritesDao(): FavoritesDao
    abstract fun matchNotificationDao(): MatchNotificationDao
}

object DatabaseProvider {
    private var instance: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return instance ?: synchronized(this) {
            val db = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "worldcup_globe_db"
            ).fallbackToDestructiveMigration().build()
            instance = db
            db
        }
    }
}
