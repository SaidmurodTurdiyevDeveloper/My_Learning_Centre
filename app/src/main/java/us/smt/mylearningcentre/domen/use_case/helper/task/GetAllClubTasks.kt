package us.smt.mylearningcentre.domen.use_case.helper.task

import us.smt.mylearningcentre.domen.repository.TaskRepository

@JvmInline
value class GetAllClubTasks(private val taskRepository: TaskRepository) {
    operator fun invoke(clubId: String) = taskRepository.getClubTasks(clubId = clubId)
}