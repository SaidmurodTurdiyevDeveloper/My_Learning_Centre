package us.smt.mylearningcentre.domen.repository

import kotlinx.coroutines.flow.Flow
import us.smt.mylearningcentre.data.model.CreateTaskData
import us.smt.mylearningcentre.data.model.TaskData
import us.smt.mylearningcentre.util.ResponseResult

interface TaskRepository {
    fun getClubTasks(clubId: String): Flow<ResponseResult<List<TaskData>>>
    fun addTask(task: CreateTaskData): Flow<ResponseResult<Boolean>>
    fun updateTask(task: TaskData): Flow<ResponseResult<Boolean>>
    fun deleteTask(task: TaskData): Flow<ResponseResult<Boolean>>
}