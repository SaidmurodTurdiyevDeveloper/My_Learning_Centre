package us.smt.mylearningcentre.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "student_data")
data class StudentData(
    @PrimaryKey val id: String,
    val email: String,
    val name: String,
    val surname: String,
    val clubId: String,
    val isWaitingApplication: String,
    val fcmToken: String,
    val isPresident: Boolean
)
