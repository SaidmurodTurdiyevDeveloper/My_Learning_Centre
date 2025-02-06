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
        const val acceptedMember = "acceptedMember"
    }

    override fun getClubTasks(clubId: String): Flow<ResponseResult<List<TaskData>>> = flow {
        try {
            val firebase = FireBaseHelper.getInstance()
            val result = firebase.getAllData(
                collectionName = FireBaseHelper.collectionTask
            )
            val ls = result.map { document ->
                val data = document.data
                TaskData(
                    id = document.id,
                    title = data?.get(title) as? String ?: "",
                    description = data?.get(description) as? String ?: "",
                    clubId = data?.get(TaskRepositoryImpl.clubId) as? String ?: "",
                    rejectedMembers = (data?.get(rejectedMember) as? List<*>)?.filterIsInstance<String>() ?: emptyList(),
                    acceptedMembers = (data?.get(acceptedMember) as? List<*>)?.filterIsInstance<String>() ?: emptyList(),
                    isCompleted = data?.get(isComplete) as? Boolean ?: false,
                )
            }.filter {
                it.clubId == clubId
            }
            db.deleteAll()
            db.insertAll(ls)
            emit(ResponseResult.Error(NetworkError.InternetConnection(ls)))
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
                rejectedMember to emptyList<String>(),
                acceptedMember to emptyList<String>(),
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
                rejectedMembers = emptyList(),
                acceptedMembers = emptyList(),
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
            val map = mapOf(
                title to task.title,
                description to task.description,
                clubId to localStorage.clubId,
                rejectedMember to task.rejectedMembers,
                acceptedMember to task.acceptedMembers,
                isComplete to task.isCompleted
            )
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