package us.smt.mylearningcentre.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "message_data")
data class MessageData(
    @PrimaryKey val id: String,
    val clubId:String,
    val isYourMessage: Boolean,
    val senderName: String,
    val text: String,
    val senderId: String
)
