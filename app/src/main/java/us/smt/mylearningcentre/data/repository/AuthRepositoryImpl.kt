package us.smt.mylearningcentre.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import us.smt.mylearningcentre.data.database.local.shared.LocalStorage
import us.smt.mylearningcentre.data.database.remote.FireBaseHelper
import us.smt.mylearningcentre.data.model.CreateStudentData
import us.smt.mylearningcentre.data.model.StudentData
import us.smt.mylearningcentre.domen.repository.AuthRepository
import us.smt.mylearningcentre.util.NetworkError
import us.smt.mylearningcentre.util.ResponseResult
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val localStorage: LocalStorage
) : AuthRepository {
    companion object {
        const val clubId = "clubId"
        const val email = "email"
        const val name = "name"
        const val surname = "surname"
        const val fcmToken = "fcmToken"
        const val isWaitingApplication = "isWaitingApplication"
    }

    override fun login(email: String, password: String): Flow<ResponseResult<Boolean>> = flow {
        try {
            val firebase = FireBaseHelper.getInstance()
            val result = firebase.login(email, password)
            if (result) {
                val token = firebase.getFcmToken()
                if (token != null)
                    localStorage.fcmToken = token
                val users = firebase.getAllData(FireBaseHelper.collectionStudent).map { document ->
                    val data = document.data
                    StudentData(
                        id = document.id,
                        email = data?.get(AuthRepositoryImpl.email) as? String ?: "",
                        surname = data?.get(surname) as? String ?: "",
                        clubId = data?.get(clubId) as? String ?: "",
                        name = data?.get(name) as? String ?: "",
                        isWaitingApplication = data?.get(isWaitingApplication) as? String ?: "",
                        fcmToken = data?.get(fcmToken) as? String ?: "",
                        isPresident = false
                    )
                }
                val currentUser = users.firstOrNull {
                    it.email == email
                }

                if (currentUser != null) {
                    localStorage.clubId = currentUser.clubId
                    localStorage.waitingApplicationId = currentUser.isWaitingApplication
                    localStorage.userId = currentUser.id
                    localStorage.userSurname = currentUser.surname
                    localStorage.userName = currentUser.name
                    localStorage.isUserCreated = true
                    localStorage.isLoggedIn = true
                    emit(ResponseResult.Success(true))
                } else {
                    val map = mapOf(
                        AuthRepositoryImpl.email to email,
                        name to "Unknown",
                        surname to "Unknown",
                        fcmToken to (token ?: localStorage.fcmToken)
                    )
                    val userId = firebase.addNewItem(
                        map, FireBaseHelper.collectionStudent
                    )
                    localStorage.userId = userId
                    localStorage.isUserCreated = true
                    localStorage.isLoggedIn = true
                    emit(ResponseResult.Success(false))
                }
            }
        } catch (e: IOException) {
            emit(ResponseResult.Error(NetworkError.InternetConnection<Boolean>()))
        } catch (e: Exception) {
            emit(ResponseResult.Error(NetworkError.NetworkResult))
        }
    }

    override fun register(email: String, password: String, studentData: CreateStudentData): Flow<ResponseResult<Boolean>> = flow {
        try {
            val firebase = FireBaseHelper.getInstance()
            val result = firebase.register(email, password)
            if (result) {
                val token = firebase.getFcmToken()
                if (token != null)
                    localStorage.fcmToken = token
                val map = mapOf(
                    AuthRepositoryImpl.email to email,
                    name to studentData.name,
                    surname to studentData.surname,
                    fcmToken to (token ?: localStorage.fcmToken)
                )
                val userId = firebase.addNewItem(
                    map, FireBaseHelper.collectionStudent
                )
                localStorage.userId = userId
                localStorage.userName = studentData.name
                localStorage.userSurname = studentData.surname
                localStorage.isUserCreated = true
                emit(ResponseResult.Success(true))
            } else {
                emit(ResponseResult.Success(false))
            }
        } catch (e: IOException) {
            emit(ResponseResult.Error(NetworkError.InternetConnection<Boolean>()))
        } catch (e: Exception) {
            emit(ResponseResult.Error(NetworkError.NetworkResult))
        }

    }
}