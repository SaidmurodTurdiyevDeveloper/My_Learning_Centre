package us.smt.mylearningcentre.domen.use_case.helper.auth

import kotlinx.coroutines.flow.Flow
import us.smt.mylearningcentre.domen.repository.AuthRepository
import us.smt.mylearningcentre.util.ResponseResult

@JvmInline
value class Login(private val authRepository: AuthRepository) {
    operator fun invoke(email: String, password: String): Flow<ResponseResult<Boolean>> = authRepository.login(email = email, password = password)
}