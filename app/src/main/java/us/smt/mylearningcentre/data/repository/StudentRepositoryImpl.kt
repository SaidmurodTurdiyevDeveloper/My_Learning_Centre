package us.smt.mylearningcentre.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import us.smt.mylearningcentre.data.database.local.room.dao.ClubDao
import us.smt.mylearningcentre.data.database.local.room.dao.StudentDao
import us.smt.mylearningcentre.data.database.local.shared.LocalStorage
import us.smt.mylearningcentre.data.database.remote.FireBaseHelper
import us.smt.mylearningcentre.data.model.ClubDetailsData
import us.smt.mylearningcentre.data.model.StudentData
import us.smt.mylearningcentre.data.repository.ClubRepositoryImpl.Companion.category
import us.smt.mylearningcentre.data.repository.ClubRepositoryImpl.Companion.description
import us.smt.mylearningcentre.data.repository.ClubRepositoryImpl.Companion.membersIdList
import us.smt.mylearningcentre.data.repository.ClubRepositoryImpl.Companion.name
import us.smt.mylearningcentre.data.repository.ClubRepositoryImpl.Companion.presidentId
import us.smt.mylearningcentre.domen.repository.StudentRepository
import us.smt.mylearningcentre.util.NetworkError
import us.smt.mylearningcentre.util.ResponseResult
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StudentRepositoryImpl @Inject constructor(
    private val localStorage: LocalStorage,
    private val studentDB: StudentDao,
    private val clubDB: ClubDao
) : StudentRepository {

    override fun getAllClubStudent(clubId: String): Flow<ResponseResult<List<StudentData>>> = flow {
        try {
            val fireBase = FireBaseHelper.getInstance()
            val document = fireBase.getDatWithId(
                id = clubId,
                collectionName = FireBaseHelper.collectionClub
            )
            val data = document?.data
            val resultData = ClubDetailsData(
                id = document?.id ?: "",
                name = data?.get(name) as? String ?: "",
                category = data?.get(category) as? String ?: "",
                presidentId = data?.get(presidentId) as? String ?: "",
                description = data?.get(description) as? String ?: "",
                members = (data?.get(membersIdList) as? List<*>)?.filterIsInstance<String>() ?: emptyList()
            )
            val idList = resultData.members
            val students = ArrayList<StudentData>()
            for (item in idList) {
                val r = fireBase.getDatWithId(item, FireBaseHelper.collectionStudent)
                if (r != null) {
                    students.add(
                        StudentData(
                            id = r.id,
                            name = data?.get(AuthRepositoryImpl.name) as? String ?: "",
                            email = data?.get(AuthRepositoryImpl.email) as? String ?: "",
                            surname = data?.get(AuthRepositoryImpl.surname) as? String ?: "",
                            clubId = data?.get(AuthRepositoryImpl.clubId) as? String ?: "",
                            isPresident = resultData.presidentId == r.id,
                            isWaitingApplication = data?.get(AuthRepositoryImpl.isWaitingApplication) as? String ?: "",
                            fcmToken = data?.get(AuthRepositoryImpl.fcmToken) as? String ?: ""
                        )
                    )
                }
            }
            emit(ResponseResult.Success(students))
        } catch (e: IOException) {
            val clubLocal = clubDB.getById(clubId)
            val ls = clubLocal?.members?.mapNotNull {
                studentDB.getById(it)
            } ?: emptyList()
            emit(ResponseResult.Error(NetworkError.InternetConnection(ls)))
        } catch (e: Exception) {
            emit(ResponseResult.Error(NetworkError.NetworkResult))
        }
    }


    override fun getStudent(userId: String): Flow<ResponseResult<StudentData>> = flow {
        try {
            val fireBaseHelper = FireBaseHelper.getInstance()
            val document = fireBaseHelper.getDatWithId(
                collectionName = FireBaseHelper.collectionStudent,
                id = userId
            )
            val data = document?.data
            val result = StudentData(
                id = document?.id ?: "",
                name = data?.get(AuthRepositoryImpl.name) as? String ?: "",
                email = data?.get(AuthRepositoryImpl.email) as? String ?: "",
                surname = data?.get(AuthRepositoryImpl.surname) as? String ?: "",
                clubId = data?.get(AuthRepositoryImpl.clubId) as? String ?: "",
                isPresident = false,
                isWaitingApplication = data?.get(AuthRepositoryImpl.isWaitingApplication) as? String ?: "",
                fcmToken = data?.get(AuthRepositoryImpl.fcmToken) as? String ?: ""
            )
            val club = fireBaseHelper.getDatWithId(result.clubId, FireBaseHelper.collectionClub)?.data?.get(presidentId)
            emit(ResponseResult.Success(result.copy(isPresident = club == userId)))
        } catch (e: IOException) {
            val data = studentDB.getById(userId)
            emit(ResponseResult.Error(NetworkError.InternetConnection(data)))
        } catch (e: Exception) {
            emit(ResponseResult.Error(NetworkError.NetworkResult))
        }
    }

    override fun getUser(): Flow<ResponseResult<StudentData>> = flow {
        try {

            val fireBaseHelper = FireBaseHelper.getInstance()
            val document = fireBaseHelper.getDatWithId(
                collectionName = FireBaseHelper.collectionStudent,
                id = localStorage.userId
            )
            val data = document?.data
            val result = StudentData(
                id = document?.id ?: "",
                name = data?.get(AuthRepositoryImpl.name) as? String ?: "",
                email = data?.get(AuthRepositoryImpl.email) as? String ?: "",
                surname = data?.get(AuthRepositoryImpl.surname) as? String ?: "",
                clubId = data?.get(AuthRepositoryImpl.clubId) as? String ?: "",
                isPresident = false,
                isWaitingApplication = data?.get(AuthRepositoryImpl.isWaitingApplication) as? String ?: "",
                fcmToken = data?.get(AuthRepositoryImpl.fcmToken) as? String ?: ""
            )
            val club = fireBaseHelper.getDatWithId(result.clubId, FireBaseHelper.collectionClub)?.data?.get(presidentId)
            emit(ResponseResult.Success(result.copy(isPresident = club == localStorage.userId)))
        } catch (e: IOException) {
            val data = studentDB.getById(localStorage.userId)
            emit(ResponseResult.Error(NetworkError.InternetConnection(data)))
        } catch (e: Exception) {
            emit(ResponseResult.Error(NetworkError.NetworkResult))
        }
    }
}