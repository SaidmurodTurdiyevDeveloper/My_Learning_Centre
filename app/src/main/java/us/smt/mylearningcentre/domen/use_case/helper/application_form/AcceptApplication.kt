package us.smt.mylearningcentre.domen.use_case.helper.application_form

import kotlinx.coroutines.flow.Flow
import us.smt.mylearningcentre.data.model.ApplicationFormData
import us.smt.mylearningcentre.domen.repository.ApplicationRepository
import us.smt.mylearningcentre.util.ResponseResult

@JvmInline
value class AcceptApplication(private val applicationRepository: ApplicationRepository) {
    operator fun invoke(data: ApplicationFormData): Flow<ResponseResult<Boolean>> = applicationRepository.acceptApplication(data)
}