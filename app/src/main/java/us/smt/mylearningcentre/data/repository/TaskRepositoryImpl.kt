package us.smt.mylearningcentre.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import us.smt.mylearningcentre.data.database.local.room.dao.TaskDao
import us.smt.mylearningcentre.data.database.local.shared.LocalStorage
import us.smt.mylearningcentre.data.database.remote.FireBaseHelper
import us.smt.mylearningcentre.data.model.CreateTaskData
import us.smt.mylearningcentre.data.model.TaskData
import us.smt.mylearningcentre.domen.repository.TaskRepository
import us.smt.mylearningcentre.util.NetworkError
import us.smt.mylearningcentre.util.ResponseResult
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepositoryImpl @Inject constructor(
    private val localStorage: LocalStorage,
    private val db: TaskDao
) : TaskRepository {
    companion object {
        const val description = "description"
        const val title = "title"
        const val clubId = "clubId"
        const val isComplete = "isComplete"
        const val rejectedMember = "rejectedMember"
        const val rejectedMemberName = "rejectedMemberName"
        const val acceptedMember = "acceptedMember"
        const val acceptedMemberName = "acceptedMemberName"
    }

    override fun getClubTasks(clubId: String): Flow<ResponseResult<List<TaskData>>> = flow {
        try {
            val firebase = FireBaseHelper.getInstance()
            val result = firebase.getAllData(
                collectionName = FireBaseHelper.collectionTask
            )

            val ls = result.map { document ->
                val data = document.data
                val joinedMembers = (data?.get(acceptedMember) as? List<*>)?.filterIsInstance<String>() ?: emptyList()
                val joinedMemberNames = (data?.get(acceptedMemberName) as? List<*>)?.filterIsInstance<String>() ?: emptyList()
                val rejectedMembers = (data?.get(rejectedMember) as? List<*>)?.filterIsInstance<String>() ?: emptyList()
                val rejectedMemberNames = (data?.get(rejectedMemberName) as? List<*>)?.filterIsInstance<String>() ?: emptyList()
                val isYouJoined = joinedMembers.contains(localStorage.userId)
                val isYouRejected = rejectedMembers.contains(localStorage.userId)
                TaskData(
                    id = document.id,
                    title = data?.get(title) as? String ?: "",
                    isYouJoined = isYouJoined,
                    isYouRejected = isYouRejected,
                    description = data?.get(description) as? String ?: "",
                    clubId = data?.get(TaskRepositoryImpl.clubId) as? String ?: "",
                    rejectedMembers = rejectedMembers.filter { it.isNotBlank() },
                    rejectedMemberNames = rejectedMemberNames.filter { it.isNotBlank() },
                    acceptedMembers = joinedMembers.filter { it.isNotBlank() },
                    acceptedMemberNames = joinedMemberNames.filter { it.isNotBlank() },
                    isCompleted = data?.get(isComplete) as? Boolean ?: false,
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

    override fun addTask(task: CreateTaskData): Flow<ResponseResult<Boolean>> = flow {
        try {
            val firebase = FireBaseHelper.getInstance()
            val map = mapOf(
                title to task.title,
                description to task.description,
                clubId to localStorage.clubId,
                isComplete to false,
                rejectedMember to emptyList<String>(),
                rejectedMemberName to emptyList<String>(),
                acceptedMember to emptyList<String>(),
                acceptedMemberName to emptyList<String>(),
            )
            val id = firebase.addNewItem(
                mapData = map,
                collectionName = FireBaseHelper.collectionTask
            )
            val newData = TaskData(
                id = id,
                title = task.title,
                description = task.description,
                clubId = localStorage.clubId,
                isYouJoined = false,
                isYouRejected = false,
                rejectedMembers = emptyList(),
                rejectedMemberNames = emptyList(),
                acceptedMembers = emptyList(),
                acceptedMemberNames = emptyList(),
                isCompleted = false
            )
            db.insert(newData)
            emit(ResponseResult.Success(true))
        } catch (e: IOException) {
            emit(ResponseResult.Error(NetworkError.InternetConnection<Boolean>()))
        } catch (e: Exception) {
            emit(ResponseResult.Error(NetworkError.NetworkResult))
        }
    }

    override fun updateTask(task: TaskData): Flow<ResponseResult<Boolean>> = flow {
        try {
            val firebase = FireBaseHelper.getInstance()
            val map = hashMapOf<String, Any>(
                title to task.title,
                description to task.description,
                clubId to localStorage.clubId
            )
            if (task.isCompleted) {
                map[isComplete] = true
                map[acceptedMember] = task.acceptedMembers
                map[acceptedMemberName] = task.acceptedMemberNames
                map[rejectedMember] = task.rejectedMembers
                map[rejectedMemberName] = task.rejectedMemberNames

            } else if (task.isYouJoined) {
                map[isComplete] = false
                map[acceptedMember] = task.acceptedMembers + localStorage.userId
                map[acceptedMemberName] = task.acceptedMemberNames + localStorage.userName
                map[rejectedMember] = task.rejectedMembers
                map[rejectedMemberName] = task.rejectedMemberNames
            } else if (task.isYouRejected) {
                map[isComplete] = false
                map[acceptedMember] = task.acceptedMembers
                map[acceptedMemberName] = task.acceptedMemberNames
                map[rejectedMember] = task.rejectedMembers + localStorage.userId
                map[rejectedMemberName] = task.rejectedMemberNames + localStorage.userName
            } else {
                map[isComplete] = false
                map[acceptedMember] = task.acceptedMembers
                map[acceptedMemberName] = task.acceptedMemberNames
                map[rejectedMember] = task.rejectedMembers
                map[rejectedMemberName] = task.rejectedMemberNames
            }
            firebase.updateItem(
                id = task.id,
                newMap = map,
                collectionName = FireBaseHelper.collectionTask
            )
            db.update(task)
            emit(ResponseResult.Success(true))
        } catch (e: IOException) {
            emit(ResponseResult.Error(NetworkError.InternetConnection<Boolean>()))
        } catch (e: Exception) {
            emit(ResponseResult.Error(NetworkError.NetworkResult))
        }
    }

    override fun deleteTask(task: TaskData): Flow<ResponseResult<Boolean>> = flow {
        try {
            val firebase = FireBaseHelper.getInstance()
            firebase.deleteItem(
                id = task.id,
                collectionName = FireBaseHelper.collectionTask
            )
            db.delete(task)
            emit(ResponseResult.Success(true))
        } catch (e: IOException) {
            emit(ResponseResult.Error(NetworkError.InternetConnection<Boolean>()))
        } catch (e: Exception) {
            emit(ResponseResult.Error(NetworkError.NetworkResult))
        }
    }
}