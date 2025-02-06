package us.smt.mylearningcentre.domen.use_case.helper.club

import us.smt.mylearningcentre.domen.repository.ClubRepository

@JvmInline
value class GetClubDetails(private val clubRepository: ClubRepository) {
    operator fun invoke(id: String) = clubRepository.getClubDetail(id = id)
}