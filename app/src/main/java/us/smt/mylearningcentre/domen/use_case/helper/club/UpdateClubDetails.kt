package us.smt.mylearningcentre.domen.use_case.helper.club

import us.smt.mylearningcentre.data.model.ClubDetailsData
import us.smt.mylearningcentre.domen.repository.ClubRepository

@JvmInline
value class UpdateClubDetails(private val clubRepository: ClubRepository) {
    operator fun invoke(data: ClubDetailsData) = clubRepository.updateClubDetail(data = data)
}