package us.smt.mylearningcentre.data.database.local.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import us.smt.mylearningcentre.data.model.StudentData

@Dao
interface StudentDao {

    // Insert a single StudentData
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(student: StudentData)

    // Insert a list of StudentData
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(students: List<StudentData>)

    // Delete a single StudentData
    @Delete
    suspend fun delete(student: StudentData)

    // Delete all StudentData
    @Query("DELETE FROM student_data")
    suspend fun deleteAll()

    // Update a single StudentData
    @Update
    suspend fun update(student: StudentData)

    // Get StudentData by ID
    @Query("SELECT * FROM student_data WHERE id = :id")
    suspend fun getById(id: String): StudentData?

    // Get all StudentData
    @Query("SELECT * FROM student_data")
    suspend fun getAll(): List<StudentData>
}
