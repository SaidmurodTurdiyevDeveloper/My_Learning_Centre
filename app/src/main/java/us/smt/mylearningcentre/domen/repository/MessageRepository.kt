package us.smt.mylearningcentre.domen.repository

import kotlinx.coroutines.flow.Flow
import us.smt.mylearningcentre.data.model.MessageData
import us.smt.mylearningcentre.util.ResponseResult

interface MessageRepository {
    fun sendMessage(message: String): Flow<ResponseResult<Boolean>>
    fun getMessages(clubId: String): Flow<ResponseResult<List<MessageData>>>
}