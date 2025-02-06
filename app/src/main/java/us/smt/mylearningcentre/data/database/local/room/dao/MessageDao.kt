package us.smt.mylearningcentre.data.database.local.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import us.smt.mylearningcentre.data.model.MessageData

@Dao
interface MessageDao {

    // Insert a single MessageData
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(message: MessageData)

    // Insert a list of MessageData
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(messages: List<MessageData>)

    // Delete a single MessageData
    @Delete
    suspend fun delete(message: MessageData)

    // Delete all MessageData
    @Query("DELETE FROM message_data")
    suspend fun deleteAll()

    // Update a single MessageData
    @Update
    suspend fun update(message: MessageData)

    // Get MessageData by ID
    @Query("SELECT * FROM message_data WHERE id = :id")
    suspend fun getById(id: String): MessageData?

    // Get all MessageData
    @Query("SELECT * FROM message_data")
    suspend fun getAll(): List<MessageData>
}
