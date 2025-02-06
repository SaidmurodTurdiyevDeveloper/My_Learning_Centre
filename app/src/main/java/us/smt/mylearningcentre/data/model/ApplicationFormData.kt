package us.smt.mylearningcentre.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "application_form_data")
data class ApplicationFormData(
    @PrimaryKey val id: String,
    val isAccepted: Boolean,
    /**
     * this need for update Student information after accept or reject application form
     */
    val studentId: String,
    val clubId: String,
    val fcmToken: String,
    val description: String
)
