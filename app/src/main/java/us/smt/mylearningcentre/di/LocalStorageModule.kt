package us.smt.mylearningcentre.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import us.smt.mylearningcentre.data.database.local.room.AppDatabase
import us.smt.mylearningcentre.data.database.local.room.dao.ApplicationFormDao
import us.smt.mylearningcentre.data.database.local.room.dao.ClubCategoryDao
import us.smt.mylearningcentre.data.database.local.room.dao.ClubDao
import us.smt.mylearningcentre.data.database.local.room.dao.CommentDao
import us.smt.mylearningcentre.data.database.local.room.dao.MessageDao
import us.smt.mylearningcentre.data.database.local.room.dao.StudentDao
import us.smt.mylearningcentre.data.database.local.room.dao.TaskDao
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object LocalStorageModule {

    @Singleton
    @Provides
    fun getAppDatabase(application: Application): AppDatabase = Room.databaseBuilder(
        application,
        AppDatabase::class.java,
        "my_learning_center_local_database"
    ).build()

    @Singleton
    @Provides
    fun getStudentDao(database: AppDatabase): StudentDao = database.getStudentDao()

    @Singleton
    @Provides
    fun getApplicationFormDao(database: AppDatabase): ApplicationFormDao = database.getApplicationFormDao()

    @Singleton
    @Provides
    fun getClubCategoryDao(database: AppDatabase): ClubCategoryDao = database.getClubCategoryDao()

    @Singleton
    @Provides
    fun getClubDao(database: AppDatabase): ClubDao = database.getClubDao()

    @Singleton
    @Provides
    fun getCommentDao(database: AppDatabase): CommentDao = database.getCommentDao()

    @Singleton
    @Provides
    fun getMessageDao(database: AppDatabase): MessageDao = database.getMessageDao()

    @Singleton
    @Provides
    fun getTaskDao(database: AppDatabase): TaskDao = database.getTaskDao()
}

