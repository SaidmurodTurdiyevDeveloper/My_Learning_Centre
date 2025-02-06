package us.smt.mylearningcentre.domen.use_case.helper.club

import us.smt.mylearningcentre.domen.repository.ClubRepository

@JvmInline
value class GetClubCategoriesList(private val clubRepository: ClubRepository) {
    operator fun invoke() = clubRepository.getClubCategory()
}