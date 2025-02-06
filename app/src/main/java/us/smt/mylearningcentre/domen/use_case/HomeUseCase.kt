package us.smt.mylearningcentre.domen.use_case

import us.smt.mylearningcentre.domen.use_case.helper.application_form.GetClubApplications
import us.smt.mylearningcentre.domen.use_case.helper.club.GetClubDetails
import us.smt.mylearningcentre.domen.use_case.helper.club.SendNotificationToMember
import us.smt.mylearningcentre.domen.use_case.helper.student.GetUser
import us.smt.mylearningcentre.domen.use_case.helper.task.AddTask
import us.smt.mylearningcentre.domen.use_case.helper.task.DeleteTask
import us.smt.mylearningcentre.domen.use_case.helper.task.GetAllClubTasks
import us.smt.mylearningcentre.domen.use_case.helper.task.UpdateTask

data class HomeUseCase(
    val getAllClubTasks: GetAllClubTasks,
    val getClubApplications: GetClubApplications,
    val addTask: AddTask,
    val updateTas: UpdateTask,
    val deleteTask: DeleteTask,
    val getUser: GetUser,
    val getClubDetails: GetClubDetails,
    val sendNotificationToMember: SendNotificationToMember
)
