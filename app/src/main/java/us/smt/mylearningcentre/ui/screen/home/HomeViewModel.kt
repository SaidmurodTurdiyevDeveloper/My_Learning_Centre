package us.smt.mylearningcentre.ui.screen.home

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import us.smt.mylearningcentre.data.model.ApplicationFormData
import us.smt.mylearningcentre.data.model.TaskData
import us.smt.mylearningcentre.domen.use_case.HomeUseCase
import us.smt.mylearningcentre.ui.screen.task.CreateTaskScreen
import us.smt.mylearningcentre.ui.utils.AppNavigator
import us.smt.mylearningcentre.ui.utils.BaseViewModel
import us.smt.mylearningcentre.util.ResponseResult
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    navigator: AppNavigator,
    private val useCase: HomeUseCase
) : BaseViewModel<HomeState, HomeIntent>(HomeState(), navigator) {

    override fun onAction(intent: HomeIntent) {
        when (intent) {
            HomeIntent.Init -> initData()
            HomeIntent.OpenAddTaskScreen -> openAddTaskScreen()
            is HomeIntent.AcceptApplication -> acceptApplication(intent.data)
            is HomeIntent.AcceptTask -> acceptTask(intent.data)
            is HomeIntent.CompleteTask -> completeTask(intent.data)
            is HomeIntent.DeclineApplication -> declineApplication(intent.data)
            is HomeIntent.DeclineTask -> declineTask(intent.data)
            is HomeIntent.DeleteTask -> deleteTask(intent.data)
        }
    }

    private fun openAddTaskScreen() {
        navigate(CreateTaskScreen())
    }

    private fun acceptApplication(data: ApplicationFormData) {
        useCase.acceptApplication(data).onEach { result ->
            when (result) {
                is ResponseResult.Error -> update(state = state.value.copy(error = result.error))
                is ResponseResult.Success -> {
                    val ls = state.value.appForm.filter {
                        it.id != data.id
                    }
                    update(state = state.value.copy(appForm = ls))
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun acceptTask(data: TaskData) {
        useCase.updateTask(
            data.copy(
                isYouJoined = true
            )
        ).onEach { result ->
            when (result) {
                is ResponseResult.Error -> update(state = state.value.copy(error = result.error))
                is ResponseResult.Success -> {
                    val ls = state.value.tasks.map {
                        if (it.id == data.id) {
                            it.copy(isYouJoined = true, isYouRejected = false)
                        } else {
                            it
                        }
                    }
                    update(state = state.value.copy(tasks = ls))
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun completeTask(data: TaskData) {
        useCase.updateTask(
            data.copy(
                isCompleted = true
            )
        ).onEach { result ->
            when (result) {
                is ResponseResult.Error -> update(state = state.value.copy(error = result.error))
                is ResponseResult.Success -> {
                    val ls = state.value.tasks.map {
                        if (it.id == data.id) {
                            it.copy(isCompleted = true)
                        } else {
                            it
                        }
                    }
                    update(state = state.value.copy(tasks = ls))
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun declineApplication(data: ApplicationFormData) {
        useCase.rejectApplication(data).onEach { result ->
            when (result) {
                is ResponseResult.Error -> update(state = state.value.copy(error = result.error))
                is ResponseResult.Success -> {
                    val ls = state.value.appForm.filter {
                        it.id != data.id
                    }
                    update(state = state.value.copy(appForm = ls))
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun declineTask(data: TaskData) {
        useCase.updateTask(
            data.copy(
                isYouJoined = false
            )
        ).onEach { result ->
            when (result) {
                is ResponseResult.Error -> update(state = state.value.copy(error = result.error))
                is ResponseResult.Success -> {
                    val ls = state.value.tasks.map {
                        if (it.id == data.id) {
                            it.copy(isYouJoined = false, isYouRejected = true)
                        } else {
                            it
                        }
                    }
                    update(state = state.value.copy(tasks = ls))
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun deleteTask(data: TaskData) {
        useCase.deleteTask(data).onEach { result ->
            when (result) {
                is ResponseResult.Error -> update(state = state.value.copy(error = result.error))
                is ResponseResult.Success -> {
                    val ls = state.value.tasks.filter {
                        it.id != data.id
                    }
                    update(state = state.value.copy(tasks = ls))
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun initData() {
        useCase.getUser().onEach { result ->
            when (result) {
                is ResponseResult.Error -> update(state = state.value.copy(error = result.error))
                is ResponseResult.Success -> {
                    update(state = state.value.copy(user = result.data))
                    if (result.data.isPresident) {
                        getApplicationForm(clubId = result.data.clubId)
                    } else {
                        getTasks(clubId = result.data.clubId)
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getTasks(clubId: String) {
        useCase.getAllClubTasks(clubId = clubId).onEach { result ->
            when (result) {
                is ResponseResult.Error -> update(state = state.value.copy(error = result.error))
                is ResponseResult.Success -> {
                    update(state = state.value.copy(tasks = result.data))
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getApplicationForm(clubId: String) {
        useCase.getClubApplications(clubId = clubId).onEach { result ->
            when (result) {
                is ResponseResult.Error -> update(state = state.value.copy(error = result.error))
                is ResponseResult.Success -> {
                    update(state = state.value.copy(appForm = result.data))
                    getTasks(clubId = clubId)
                }
            }
        }.launchIn(viewModelScope)
    }
}