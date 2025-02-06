package us.smt.mylearningcentre.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import us.smt.mylearningcentre.data.database.local.room.converter.StringListConverter

@Entity(tableName = "task_data")
data class TaskData(
    @PrimaryKey val id: String,
    val clubId: String,
    val title: String,
    val isCompleted: Boolean,
    val description: String,
    @TypeConverters(StringListConverter::class) val acceptedMembers: List<String>,
    @TypeConverters(StringListConverter::class) val rejectedMembers: List<String>
)
