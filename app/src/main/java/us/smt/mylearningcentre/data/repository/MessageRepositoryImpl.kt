package us.smt.mylearningcentre.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import us.smt.mylearningcentre.data.database.local.room.dao.MessageDao
import us.smt.mylearningcentre.data.database.local.shared.LocalStorage
import us.smt.mylearningcentre.data.database.remote.FireBaseHelper
import us.smt.mylearningcentre.data.model.MessageData
import us.smt.mylearningcentre.domen.repository.MessageRepository
import us.smt.mylearningcentre.util.NetworkError
import us.smt.mylearningcentre.util.ResponseResult
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessageRepositoryImpl @Inject constructor(
    private val localStorage: LocalStorage,
    private val db: MessageDao
) : MessageRepository {
    companion object {
        const val senderName = "senderName"
        const val senderId = "senderId"
        const val text = "message"
        const val clubId = "message"
    }

    override fun sendMessage(message: String): Flow<ResponseResult<Boolean>> = flow {
        try {
            val fireBaseHelper = FireBaseHelper.getInstance()
            val map = hashMapOf(
                clubId to localStorage.clubId,
                text to message,
                senderId to localStorage.userId,
                senderName to localStorage.userName
            )

            val id = fireBaseHelper.addNewItem(
                mapData = map,
                collectionName = FireBaseHelper.collectionMessage
            )

            val createData = MessageData(
                id = id,
                isYourMessage = true,
                clubId = localStorage.clubId,
                senderName = localStorage.userName,
                senderId = localStorage.userId,
                text = message
            )
            db.insert(createData)
            emit(ResponseResult.Success(id.isNotBlank()))
        } catch (e: IOException) {
            emit(ResponseResult.Error(NetworkError.InternetConnection<Boolean>()))
        } catch (e: Exception) {
            emit(ResponseResult.Error(NetworkError.NetworkResult))
        }
    }

    override fun getMessages(clubId: String): Flow<ResponseResult<List<MessageData>>> = flow {
        try {

            val fireBaseHelper = FireBaseHelper.getInstance()
            val result = fireBaseHelper.getAllData(
                collectionName = FireBaseHelper.collectionMessage
            )
            val ls = result.map { document ->
                val data = document.data
                val messageUserId = data?.get(senderId) as? String ?: ""
                MessageData(
                    id = document.id,
                    senderName = data?.get(senderName) as? String ?: "",
                    senderId = messageUserId,
                    text = data?.get(text) as? String ?: "",
                    clubId = data?.get(MessageRepositoryImpl.clubId) as? String ?: "",
                    isYourMessage = messageUserId == localStorage.userId
                )
            }.filter {
                it.clubId == clubId
            }
            db.deleteAll()
            db.insertAll(ls)
            emit(ResponseResult.Success(ls))
        } catch (e: IOException) {
            val ls = db.getAll()
            emit(ResponseResult.Error(NetworkError.InternetConnection(ls)))
        } catch (e: Exception) {
            emit(ResponseResult.Error(NetworkError.NetworkResult))
        }
    }

}