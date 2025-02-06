package us.smt.mylearningcentre.data.database.local.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import us.smt.mylearningcentre.data.model.TaskData

@Dao
interface TaskDao {

    // Insert a single TaskData
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: TaskData)

    // Insert a list of TaskData
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(tasks: List<TaskData>)

    // Delete a single TaskData
    @Delete
    suspend fun delete(task: TaskData)

    // Delete all TaskData
    @Query("DELETE FROM task_data")
    suspend fun deleteAll()

    // Update a single TaskData
    @Update
    suspend fun update(task: TaskData)

    // Get TaskData by ID
    @Query("SELECT * FROM task_data WHERE id = :id")
    suspend fun getById(id: String): TaskData?

    // Get all TaskData
    @Query("SELECT * FROM task_data")
    suspend fun getAll(): List<TaskData>
}
