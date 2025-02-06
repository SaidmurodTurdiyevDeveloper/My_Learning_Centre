package us.smt.mylearningcentre.domen.use_case.helper.application_form

import kotlinx.coroutines.flow.Flow
import us.smt.mylearningcentre.data.model.CreateApplicationFormData
import us.smt.mylearningcentre.domen.repository.ApplicationRepository
import us.smt.mylearningcentre.util.ResponseResult

@JvmInline
value class CreateApplicationToJoinClub(private val applicationRepository: ApplicationRepository) {
    operator fun invoke(data: CreateApplicationFormData): Flow<ResponseResult<Boolean>> = applicationRepository.createApplicationToJoinClub(data)
}