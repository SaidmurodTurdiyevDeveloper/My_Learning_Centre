package us.smt.mylearningcentre.domen.use_case.helper.message

import us.smt.mylearningcentre.domen.repository.MessageRepository

@JvmInline
value class SendMessage(private val messageRepository: MessageRepository) {
    operator fun invoke(message: String) = messageRepository.sendMessage(message = message)
}