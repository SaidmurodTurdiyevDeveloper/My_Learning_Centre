package us.smt.mylearningcentre.domen.use_case.helper.application_form

import us.smt.mylearningcentre.domen.repository.ApplicationRepository

@JvmInline
value class GetClubApplications(private val applicationRepository: ApplicationRepository) {
    operator fun invoke(clubId: String) = applicationRepository.getClubApplications(clubId)
}