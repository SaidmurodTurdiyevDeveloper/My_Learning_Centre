package us.smt.mylearningcentre.domen.repository

import kotlinx.coroutines.flow.Flow
import us.smt.mylearningcentre.data.model.CreateStudentData
import us.smt.mylearningcentre.util.ResponseResult

interface AuthRepository {
    fun login(email: String, password: String): Flow<ResponseResult<Boolean>>
    fun register(email: String, password: String,studentData: CreateStudentData): Flow<ResponseResult<Boolean>>
}