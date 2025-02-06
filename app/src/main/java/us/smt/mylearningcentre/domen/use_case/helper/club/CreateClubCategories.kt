package us.smt.mylearningcentre.domen.use_case.helper.club

import us.smt.mylearningcentre.domen.repository.ClubRepository

@JvmInline
value class CreateClubCategories(private val clubRepository: ClubRepository) {
    operator fun invoke(name: String) = clubRepository.createClubCategory(name = name)
}