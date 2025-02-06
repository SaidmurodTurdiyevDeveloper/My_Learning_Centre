package us.smt.mylearningcentre.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import us.smt.mylearningcentre.data.database.local.room.converter.StringListConverter

@Entity(tableName = "club_data")
data class ClubDetailsData(
    @PrimaryKey val id: String,
    val name: String,
    val category: String,
    val presidentId: String,
    val description: String,
    @TypeConverters(StringListConverter::class) val members: List<String>
)

fun List<ClubDetailsData>.toClubDataLs(): List<ClubData>{
    return map {
        ClubData(
            id = it.id,
            name = it.name,
            category = it.category
        )
    }
}
