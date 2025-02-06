package us.smt.mylearningcentre.data.database.local.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import us.smt.mylearningcentre.data.model.CommentData

@Dao
interface CommentDao {

    // Insert a single CommentData
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(comment: CommentData)

    // Insert a list of CommentData
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(comments: List<CommentData>)

    // Delete a single CommentData
    @Delete
    suspend fun delete(comment: CommentData)

    // Delete all CommentData
    @Query("DELETE FROM comment_data")
    suspend fun deleteAll()

    // Update a single CommentData
    @Update
    suspend fun update(comment: CommentData)

    // Get CommentData by ID
    @Query("SELECT * FROM comment_data WHERE id = :id")
    suspend fun getById(id: String): CommentData?

    // Get all CommentData
    @Query("SELECT * FROM comment_data")
    suspend fun getAll(): List<CommentData>
}
