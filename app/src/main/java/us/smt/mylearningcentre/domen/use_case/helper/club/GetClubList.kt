package us.smt.mylearningcentre.domen.use_case.helper.club

import us.smt.mylearningcentre.domen.repository.ClubRepository

@JvmInline
value class GetClubList(private val clubRepository: ClubRepository) {
    operator fun invoke() = clubRepository.getClubList()
}