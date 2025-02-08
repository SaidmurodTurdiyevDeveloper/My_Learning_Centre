package us.smt.mylearningcentre.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import us.smt.mylearningcentre.data.database.local.room.dao.ApplicationFormDao
import us.smt.mylearningcentre.data.database.local.shared.LocalStorage
import us.smt.mylearningcentre.data.database.remote.FireBaseHelper
import us.smt.mylearningcentre.data.model.ApplicationFormData
import us.smt.mylearningcentre.data.model.CreateApplicationFormData
import us.smt.mylearningcentre.domen.repository.ApplicationRepository
import us.smt.mylearningcentre.util.NetworkError
import us.smt.mylearningcentre.util.ResponseResult
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApplicationRepositoryImpl @Inject constructor(
    private val localStorage: LocalStorage,
    private val db: ApplicationFormDao
) : ApplicationRepository {
    companion object {
        const val clubId = "clubId"
        const val studentId = "studentId"
        const val studentName = "studentName"
        const val isAccepted = "isAccepted"
        const val userFcmToken = "userFcmToken"
        const val applicationDescription = "applicationDescription"
    }

    override fun createApplicationToJoinClub(data: CreateApplicationFormData): Flow<ResponseResult<Boolean>> = flow {
        try {
            val fireBaseHelper = FireBaseHelper.getInstance()
            val map = hashMapOf(
                clubId to data.clubId,
                studentId to localStorage.userId,
                studentName to localStorage.userName,
                isAccepted to false,
                applicationDescription to data.description
            )
            val token = fireBaseHelper.getFcmToken()
            if (token != null) {
                localStorage.fcmToken = token
            }
            map[userFcmToken] = token ?: localStorage.fcmToken
            val id = fireBaseHelper.addNewItem(
                mapData = map,
                collectionName = FireBaseHelper.collectionApplication
            )

            val createData = ApplicationFormData(
                id = id,
                isAccepted = false,
                studentId = localStorage.userId,
                studentName = localStorage.userName,
                clubId = data.clubId,
                fcmToken = token ?: "",
                description = data.description
            )
            db.insert(createData)
            if (id.isNotBlank()) {
                localStorage.waitingApplicationId = id
                fireBaseHelper.updateItem(
                    localStorage.userId,
                    mapOf(
                        AuthRepositoryImpl.isWaitingApplication to id
                    ),
                    FireBaseHelper.collectionStudent
                )
            }
            emit(ResponseResult.Success(id.isNotBlank()))
        } catch (e: IOException) {
            emit(ResponseResult.Error(NetworkError.InternetConnection<Boolean>()))
        } catch (e: Exception) {
            emit(ResponseResult.Error(NetworkError.NetworkResult))
        }
    }


    override fun acceptApplication(application: ApplicationFormData): Flow<ResponseResult<Boolean>> = flow {
        try {
            val fireBaseHelper = FireBaseHelper.getInstance()
            val map = mapOf(isAccepted to true)
            fireBaseHelper.updateItem(
                id = application.id,
                newMap = map,
                collectionName = FireBaseHelper.collectionApplication
            )
            val updateStudentMap = mapOf(AuthRepositoryImpl.clubId to application.clubId, AuthRepositoryImpl.isWaitingApplication to "")

            fireBaseHelper.updateItem(
                id = application.studentId,
                newMap = updateStudentMap,
                collectionName = FireBaseHelper.collectionStudent
            )
            emit(ResponseResult.Success(true))
        } catch (e: IOException) {
            emit(ResponseResult.Error(NetworkError.InternetConnection<Boolean>()))
        } catch (e: Exception) {
            emit(ResponseResult.Error(NetworkError.NetworkResult))
        }
    }

    override fun rejectApplication(application: ApplicationFormData): Flow<ResponseResult<Boolean>> = flow {
        try {
            val fireBaseHelper = FireBaseHelper.getInstance()
            fireBaseHelper.deleteItem(
                id = application.id,
                collectionName = FireBaseHelper.collectionApplication
            )
            val updateStudentMap = mapOf(AuthRepositoryImpl.isWaitingApplication to "", AuthRepositoryImpl.clubId to "")

            fireBaseHelper.updateItem(
                id = application.studentId,
                newMap = updateStudentMap,
                collectionName = FireBaseHelper.collectionStudent
            )
            emit(ResponseResult.Success(true))
        } catch (e: IOException) {
            emit(ResponseResult.Error(NetworkError.InternetConnection<Boolean>()))
        } catch (e: Exception) {
            emit(ResponseResult.Error(NetworkError.NetworkResult))
        }
    }

    override fun getClubApplications(clubId: String): Flow<ResponseResult<List<ApplicationFormData>>> = flow {
        try {
            val fireBaseHelper = FireBaseHelper.getInstance()
            val result = fireBaseHelper.getAllData(
                collectionName = FireBaseHelper.collectionApplication
            )
            val ls = result.map { document ->
                val data = document.data
                ApplicationFormData(
                    id = document.id,
                    isAccepted = data?.get(isAccepted) as? Boolean ?: false,
                    studentId = data?.get(studentId) as? String ?: "",
                    studentName = data?.get(studentName) as? String ?: "",
                    clubId = data?.get(ApplicationRepositoryImpl.clubId) as? String ?: "",
                    fcmToken = data?.get(userFcmToken) as? String ?: "",
                    description = data?.get(applicationDescription) as? String ?: ""
                )
            }.filter {
                it.clubId == clubId
            }
            val localList = db.getAll()
            if (localList.isNotEmpty()) {
                db.deleteAll()
            }
            db.insertAll(ls)
            emit(ResponseResult.Success(ls))
        } catch (e: IOException) {
            val localList = db.getAll().filter {
                it.clubId == clubId
            }
            emit(ResponseResult.Error(NetworkError.InternetConnection(localList)))
        } catch (e: Exception) {
            emit(ResponseResult.Error(NetworkError.NetworkResult))
        }
    }

    override fun getMyApplication(applicationFormId: String): Flow<ResponseResult<ApplicationFormData>> = flow {
        try {
            val fireBaseHelper = FireBaseHelper.getInstance()
            val document = fireBaseHelper.getDatWithId(
                collectionName = FireBaseHelper.collectionApplication,
                id = applicationFormId
            )
            val responseData = document?.data
            val result = ApplicationFormData(
                id = document?.id ?: "",
                isAccepted = responseData?.get(isAccepted) as? Boolean ?: false,
                studentId = responseData?.get(studentId) as? String ?: "",
                studentName = responseData?.get(studentName) as? String ?: "",
                clubId = responseData?.get(clubId) as? String ?: "",
                fcmToken = responseData?.get(userFcmToken) as? String ?: "",
                description = responseData?.get(applicationDescription) as? String ?: ""
            )
            emit(ResponseResult.Success(result))
        } catch (e: IOException) {
            val data = db.getById(applicationFormId)
            emit(ResponseResult.Error(NetworkError.InternetConnection(data)))
        } catch (e: Exception) {
            emit(ResponseResult.Error(NetworkError.NetworkResult))
        }
    }
}