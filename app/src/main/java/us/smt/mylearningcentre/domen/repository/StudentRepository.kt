package us.smt.mylearningcentre.domen.repository

import kotlinx.coroutines.flow.Flow
import us.smt.mylearningcentre.data.model.StudentData
import us.smt.mylearningcentre.util.ResponseResult

interface StudentRepository {
    fun getAllClubStudent(clubId: String): Flow<ResponseResult<List<StudentData>>>
    fun getStudent(userId: String): Flow<ResponseResult<StudentData>>
    fun getUser(): Flow<ResponseResult<StudentData>>
}