package us.smt.mylearningcentre.domen.use_case.helper.club

import us.smt.mylearningcentre.data.model.CreateUpdateClubData
import us.smt.mylearningcentre.domen.repository.ClubRepository

@JvmInline
value class CreateClubDetails(private val clubRepository: ClubRepository) {
    operator fun invoke(data: CreateUpdateClubData) = clubRepository.createClubDetail(data = data)
}