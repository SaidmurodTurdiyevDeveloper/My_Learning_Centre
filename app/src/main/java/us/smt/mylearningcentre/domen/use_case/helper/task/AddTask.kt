package us.smt.mylearningcentre.domen.use_case.helper.task

import us.smt.mylearningcentre.data.model.CreateTaskData
import us.smt.mylearningcentre.domen.repository.TaskRepository

@JvmInline
value class AddTask(private val taskRepository: TaskRepository) {
    operator fun invoke(data: CreateTaskData) = taskRepository.addTask(task = data)
}