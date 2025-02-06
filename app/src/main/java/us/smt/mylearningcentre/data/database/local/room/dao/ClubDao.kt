package us.smt.mylearningcentre.data.database.local.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import us.smt.mylearningcentre.data.model.ClubDetailsData

@Dao
interface ClubDao {

    // Insert a single ClubData
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(club: ClubDetailsData)

    // Insert a list of ClubDetailsData
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(clubs: List<ClubDetailsData>)

    // Delete a single ClubDetailsData
    @Delete
    suspend fun delete(club: ClubDetailsData)

    // Delete all ClubDetailsData
    @Query("DELETE FROM club_data")
    suspend fun deleteAll()

    // Update a single ClubDetailsData
    @Update
    suspend fun update(club: ClubDetailsData)

    // Get ClubDetailsData by ID
    @Query("SELECT * FROM club_data WHERE id = :id")
    suspend fun getById(id: String): ClubDetailsData?

    // Get all ClubDetailsData
    @Query("SELECT * FROM club_data")
    suspend fun getAll(): List<ClubDetailsData>
}
