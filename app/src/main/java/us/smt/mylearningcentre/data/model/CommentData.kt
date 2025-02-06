package us.smt.mylearningcentre.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "comment_data")
data class CommentData(
    @PrimaryKey val id: String,
    val text: String,
    val rating: Double,
    val clubId: String
)
