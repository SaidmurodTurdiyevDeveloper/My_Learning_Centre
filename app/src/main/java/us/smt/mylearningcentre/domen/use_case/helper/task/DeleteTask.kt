package us.smt.mylearningcentre.domen.use_case.helper.task

import us.smt.mylearningcentre.data.model.TaskData
import us.smt.mylearningcentre.domen.repository.TaskRepository

@JvmInline
value class DeleteTask(private val taskRepository: TaskRepository) {
    operator fun invoke(data: TaskData) = taskRepository.deleteTask(task = data)
}