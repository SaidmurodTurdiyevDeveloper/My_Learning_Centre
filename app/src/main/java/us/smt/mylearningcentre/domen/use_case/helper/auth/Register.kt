package us.smt.mylearningcentre.domen.use_case.helper.auth

import kotlinx.coroutines.flow.Flow
import us.smt.mylearningcentre.data.model.CreateStudentData
import us.smt.mylearningcentre.domen.repository.AuthRepository
import us.smt.mylearningcentre.util.ResponseResult

@JvmInline
value class Register(private val authRepository: AuthRepository) {
    operator fun invoke(email: String, password: String, studentData: CreateStudentData): Flow<ResponseResult<Boolean>> = authRepository.register(
        email = email, password = password,
        studentData = studentData
    )
}