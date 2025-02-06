package us.smt.mylearningcentre.domen.use_case

import us.smt.mylearningcentre.domen.use_case.helper.club.GetClubDetails
import us.smt.mylearningcentre.domen.use_case.helper.club.SendNotificationToMember
import us.smt.mylearningcentre.domen.use_case.helper.message.GetMessages
import us.smt.mylearningcentre.domen.use_case.helper.message.SendMessage
import us.smt.mylearningcentre.domen.use_case.helper.student.GetUser

data class ChatUseCase(
    val getMessages: GetMessages,
    val sendMessage: SendMessage,
    val getUser: GetUser,
    val getClubDetails: GetClubDetails,
    val sendNotificationToMember: SendNotificationToMember
)
