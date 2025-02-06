package us.smt.mylearningcentre.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import us.smt.mylearningcentre.data.repository.ApplicationRepositoryImpl
import us.smt.mylearningcentre.data.repository.AuthRepositoryImpl
import us.smt.mylearningcentre.data.repository.ClubRepositoryImpl
import us.smt.mylearningcentre.data.repository.MessageRepositoryImpl
import us.smt.mylearningcentre.data.repository.StudentRepositoryImpl
import us.smt.mylearningcentre.data.repository.TaskRepositoryImpl
import us.smt.mylearningcentre.domen.repository.ApplicationRepository
import us.smt.mylearningcentre.domen.repository.AuthRepository
import us.smt.mylearningcentre.domen.repository.ClubRepository
import us.smt.mylearningcentre.domen.repository.MessageRepository
import us.smt.mylearningcentre.domen.repository.StudentRepository
import us.smt.mylearningcentre.domen.repository.TaskRepository

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    fun bindStudentRepository(impl: StudentRepositoryImpl): StudentRepository

    @Binds
    fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    fun bindClubRepository(impl: ClubRepositoryImpl): ClubRepository

    @Binds
    fun bindApplicationFormRepository(impl: ApplicationRepositoryImpl): ApplicationRepository

    @Binds
    fun bindMessageRepository(impl: MessageRepositoryImpl): MessageRepository

    @Binds
    fun bindTaskRepository(impl: TaskRepositoryImpl): TaskRepository

}