package us.smt.mylearningcentre.domen.use_case.helper.club

import us.smt.mylearningcentre.domen.repository.ClubRepository

@JvmInline
value class SendNotificationToMember(private val clubRepository: ClubRepository) {
    operator fun invoke(tokens: List<String>, title: String, body: String) = clubRepository.sendNotificationToMembers(tokens = tokens, title = title, body = body)
}