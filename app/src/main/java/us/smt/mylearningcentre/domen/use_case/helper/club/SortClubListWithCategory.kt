package us.smt.mylearningcentre.domen.use_case.helper.club

import us.smt.mylearningcentre.domen.repository.ClubRepository

@JvmInline
value class SortClubListWithCategory(private val clubRepository: ClubRepository) {
    operator fun invoke(categories: List<String>) = clubRepository.sortClubListWithCategory(categories = categories)
}