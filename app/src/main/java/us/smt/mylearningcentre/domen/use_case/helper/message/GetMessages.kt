package us.smt.mylearningcentre.domen.use_case.helper.message

import us.smt.mylearningcentre.domen.repository.MessageRepository

@JvmInline
value class GetMessages(private val messageRepository: MessageRepository) {
    operator fun invoke(clubId: String) = messageRepository.getMessages(clubId = clubId)
}