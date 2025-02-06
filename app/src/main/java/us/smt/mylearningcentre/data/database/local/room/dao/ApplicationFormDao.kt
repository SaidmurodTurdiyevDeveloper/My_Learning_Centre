package us.smt.mylearningcentre.data.database.local.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import us.smt.mylearningcentre.data.model.ApplicationFormData

@Dao
interface ApplicationFormDao {

    // Insert a single ApplicationFormData
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(applicationForm: ApplicationFormData)

    // Insert a list of ApplicationFormData
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(applicationForms: List<ApplicationFormData>)

    // Delete a single ApplicationFormData
    @Delete
    suspend fun delete(applicationForm: ApplicationFormData)

    // Delete all ApplicationFormData
    @Query("DELETE FROM application_form_data")
    suspend fun deleteAll()

    // Update a single ApplicationFormData
    @Update
    suspend fun update(applicationForm: ApplicationFormData)

    // Get ApplicationFormData by ID
    @Query("SELECT * FROM application_form_data WHERE id = :id")
    suspend fun getById(id: String): ApplicationFormData?

    // Get all ApplicationFormData
    @Query("SELECT * FROM application_form_data")
    suspend fun getAll(): List<ApplicationFormData>
}
