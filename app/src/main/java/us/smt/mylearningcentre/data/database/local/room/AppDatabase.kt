package us.smt.mylearningcentre.data.database.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import us.smt.mylearningcentre.data.database.local.room.converter.StringListConverter
import us.smt.mylearningcentre.data.database.local.room.dao.ApplicationFormDao
import us.smt.mylearningcentre.data.database.local.room.dao.ClubCategoryDao
import us.smt.mylearningcentre.data.database.local.room.dao.ClubDao
import us.smt.mylearningcentre.data.database.local.room.dao.CommentDao
import us.smt.mylearningcentre.data.database.local.room.dao.MessageDao
import us.smt.mylearningcentre.data.database.local.room.dao.StudentDao
import us.smt.mylearningcentre.data.database.local.room.dao.TaskDao
import us.smt.mylearningcentre.data.model.ApplicationFormData
import us.smt.mylearningcentre.data.model.ClubCategoryData
import us.smt.mylearningcentre.data.model.ClubDetailsData
import us.smt.mylearningcentre.data.model.CommentData
import us.smt.mylearningcentre.data.model.MessageData
import us.smt.mylearningcentre.data.model.StudentData
import us.smt.mylearningcentre.data.model.TaskData
@Database(
    entities = [
        ApplicationFormData::class,
        ClubCategoryData::class,
        ClubDetailsData::class,
        CommentData::class,
        MessageData::class,
        StudentData::class,
        TaskData::class
    ],
    version = 1
)
@TypeConverters(StringListConverter::class)  // Specify the Converters class
abstract class AppDatabase : RoomDatabase() {
    abstract fun getApplicationFormDao(): ApplicationFormDao
    abstract fun getClubCategoryDao(): ClubCategoryDao
    abstract fun getClubDao(): ClubDao
    abstract fun getCommentDao(): CommentDao
    abstract fun getMessageDao(): MessageDao
    abstract fun getStudentDao(): StudentDao
    abstract fun getTaskDao(): TaskDao
}
