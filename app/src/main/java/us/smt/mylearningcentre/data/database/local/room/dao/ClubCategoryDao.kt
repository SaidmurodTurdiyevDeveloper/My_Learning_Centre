package us.smt.mylearningcentre.data.database.local.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import us.smt.mylearningcentre.data.model.ClubCategoryData

@Dao
interface ClubCategoryDao {

    // Insert a single ClubCategoryData
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(clubCategory: ClubCategoryData)

    // Insert a list of ClubCategoryData
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(clubCategories: List<ClubCategoryData>)

    // Delete a single ClubCategoryData
    @Delete
    suspend fun delete(clubCategory: ClubCategoryData)

    // Delete all ClubCategoryData
    @Query("DELETE FROM club_category_data")
    suspend fun deleteAll()

    // Update a single ClubCategoryData
    @Update
    suspend fun update(clubCategory: ClubCategoryData)

    // Get ClubCategoryData by ID
    @Query("SELECT * FROM club_category_data WHERE id = :id")
    suspend fun getById(id: String): ClubCategoryData?

    // Get all ClubCategoryData
    @Query("SELECT * FROM club_category_data")
    suspend fun getAll(): List<ClubCategoryData>
}
