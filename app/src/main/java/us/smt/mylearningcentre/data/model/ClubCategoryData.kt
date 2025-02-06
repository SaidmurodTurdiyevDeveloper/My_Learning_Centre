package us.smt.mylearningcentre.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "club_category_data")
data class ClubCategoryData(
    @PrimaryKey val id: String,
    val name: String
)
