package us.smt.mylearningcentre.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import us.smt.mylearningcentre.domen.repository.ApplicationRepository
import us.smt.mylearningcentre.domen.repository.AuthRepository
import us.smt.mylearningcentre.domen.repository.ClubRepository
import us.smt.mylearningcentre.domen.repository.MessageRepository
import us.smt.mylearningcentre.domen.repository.StudentRepository
import us.smt.mylearningcentre.domen.repository.TaskRepository
import us.smt.mylearningcentre.domen.use_case.ChatUseCase
import us.smt.mylearningcentre.domen.use_case.ClubDetailsUseCase
import us.smt.mylearningcentre.domen.use_case.ClubListUseCase
import us.smt.mylearningcentre.domen.use_case.CreateApplicationUseCase
import us.smt.mylearningcentre.domen.use_case.CreateNewClubUseCase
import us.smt.mylearningcentre.domen.use_case.CreateTaskUseCase
import us.smt.mylearningcentre.domen.use_case.HomeUseCase
import us.smt.mylearningcentre.domen.use_case.LoginUseCase
import us.smt.mylearningcentre.domen.use_case.RegisterUseCase
import us.smt.mylearningcentre.domen.use_case.SettingUseCase
import us.smt.mylearningcentre.domen.use_case.helper.application_form.AcceptApplication
import us.smt.mylearningcentre.domen.use_case.helper.application_form.CreateApplicationToJoinClub
import us.smt.mylearningcentre.domen.use_case.helper.application_form.GetClubApplications
import us.smt.mylearningcentre.domen.use_case.helper.application_form.RejectApplication
import us.smt.mylearningcentre.domen.use_case.helper.auth.Login
import us.smt.mylearningcentre.domen.use_case.helper.auth.Register
import us.smt.mylearningcentre.domen.use_case.helper.club.CreateClubCategories
import us.smt.mylearningcentre.domen.use_case.helper.club.CreateClubDetails
import us.smt.mylearningcentre.domen.use_case.helper.club.GetClubCategoriesList
import us.smt.mylearningcentre.domen.use_case.helper.club.GetClubDetails
import us.smt.mylearningcentre.domen.use_case.helper.club.GetClubList
import us.smt.mylearningcentre.domen.use_case.helper.club.SendNotificationToMember
import us.smt.mylearningcentre.domen.use_case.helper.club.SortClubListWithCategory
import us.smt.mylearningcentre.domen.use_case.helper.message.GetMessages
import us.smt.mylearningcentre.domen.use_case.helper.message.SendMessage
import us.smt.mylearningcentre.domen.use_case.helper.student.GetAllClubStudent
import us.smt.mylearningcentre.domen.use_case.helper.student.GetStudent
import us.smt.mylearningcentre.domen.use_case.helper.student.GetUser
import us.smt.mylearningcentre.domen.use_case.helper.task.AddTask
import us.smt.mylearningcentre.domen.use_case.helper.task.DeleteTask
import us.smt.mylearningcentre.domen.use_case.helper.task.GetAllClubTasks
import us.smt.mylearningcentre.domen.use_case.helper.task.UpdateTask


@Module
@InstallIn(ViewModelComponent::class)
class UseCaseModule {
    @Provides
    fun provideHomeUseCase(
        clubRepository: ClubRepository,
        taskRepository: TaskRepository,
        studentRepository: StudentRepository,
        applicationRepository: ApplicationRepository
    ): HomeUseCase {
        return HomeUseCase(
            getAllClubTasks = GetAllClubTasks(taskRepository = taskRepository),
            updateTask = UpdateTask(taskRepository = taskRepository),
            deleteTask = DeleteTask(taskRepository = taskRepository),
            getUser = GetUser(studentRepository = studentRepository),
            sendNotificationToMember = SendNotificationToMember(clubRepository = clubRepository),
            getClubApplications = GetClubApplications(applicationRepository = applicationRepository),
            acceptApplication = AcceptApplication(applicationRepository = applicationRepository),
            rejectApplication = RejectApplication(applicationRepository = applicationRepository)
        )
    }

    @Provides
    fun provideSettingUseCase(
        studentRepository: StudentRepository
    ): SettingUseCase {
        return SettingUseCase(
            getUser = GetUser(studentRepository = studentRepository)
        )
    }

    @Provides
    fun provideChatUseCase(
        messageRepository: MessageRepository,
        clubRepository: ClubRepository,
        studentRepository: StudentRepository
    ): ChatUseCase {
        return ChatUseCase(
            getMessages = GetMessages(messageRepository = messageRepository),
            sendMessage = SendMessage(messageRepository = messageRepository),
            getUser = GetUser(studentRepository = studentRepository),
            getClubDetails = GetClubDetails(clubRepository = clubRepository),
            sendNotificationToMember = SendNotificationToMember(clubRepository = clubRepository)
        )
    }

    @Provides
    fun provideClubListUseCase(
        clubRepository: ClubRepository,
        studentRepository: StudentRepository
    ): ClubListUseCase {
        return ClubListUseCase(
            getClubList = GetClubList(clubRepository = clubRepository),
            getClubCategoriesList = GetClubCategoriesList(clubRepository = clubRepository),
            sortClubListWithCategory = SortClubListWithCategory(clubRepository = clubRepository),
            getUser = GetUser(studentRepository = studentRepository),
            createClubCategories = CreateClubCategories(clubRepository = clubRepository)
        )
    }

    @Provides
    fun provideCreateNewClubUseCase(
        clubRepository: ClubRepository
    ): CreateNewClubUseCase {
        return CreateNewClubUseCase(
            createClubCategories = CreateClubCategories(clubRepository = clubRepository),
            createClubDetails = CreateClubDetails(clubRepository = clubRepository),
            getClubCategoriesList = GetClubCategoriesList(clubRepository = clubRepository)
        )
    }

    @Provides
    fun provideLoginUseCase(
        authRepository: AuthRepository
    ): LoginUseCase {
        return LoginUseCase(
            login = Login(authRepository = authRepository)
        )
    }

    @Provides
    fun provideRegisterUseCase(
        authRepository: AuthRepository
    ): RegisterUseCase {
        return RegisterUseCase(
            register = Register(authRepository = authRepository)
        )
    }

    @Provides
    fun provideCluDetailUseCase(
        clubRepository: ClubRepository,
        studentRepository: StudentRepository,
    ): ClubDetailsUseCase {
        return ClubDetailsUseCase(
            getClubDetails = GetClubDetails(clubRepository = clubRepository),
            getAllClubStudent = GetAllClubStudent(studentRepository = studentRepository),
            getStudent = GetStudent(studentRepository = studentRepository)
        )
    }

    @Provides
    fun provideCreateApplicationUseCase(
        applicationRepository: ApplicationRepository
    ): CreateApplicationUseCase {
        return CreateApplicationUseCase(
            createApplicationToJoinClub = CreateApplicationToJoinClub(applicationRepository = applicationRepository),
        )
    }

    @Provides
    fun provideCreateTaskUseCase(
        taskRepository: TaskRepository
    ): CreateTaskUseCase {
        return CreateTaskUseCase(
            addTask = AddTask(taskRepository = taskRepository)
        )
    }
}
